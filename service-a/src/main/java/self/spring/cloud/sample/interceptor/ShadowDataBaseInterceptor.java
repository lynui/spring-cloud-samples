package self.spring.cloud.sample.interceptor;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
	    {
	    	@Signature(type = Executor.class, method = "update",args = { MappedStatement.class,Object.class }),
	    	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	    }
	)
public class ShadowDataBaseInterceptor implements Interceptor {

	private final static Logger logger = LoggerFactory.getLogger(ShadowDataBaseInterceptor.class);

	private Properties properties;

	private static String SHADOW_DATABASE = "shadowDataBase";
	private static int MAPPED_STATEMENT_INDEX = 0;// 这是对应上面的args的序号
	private static int PARAMETER_INDEX = 1;
	private static int ROWBOUNDS_INDEX = 2;
	private static int RESULT_HANDLER_INDEX = 3;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object[] args = invocation.getArgs();
		
		String methodName = invocation.getMethod().getName();
		Object parameter = args[PARAMETER_INDEX];
		int shadowDataBase = 1;
		if (null != parameter) {
			if (Map.class.isAssignableFrom(parameter.getClass())) {
				@SuppressWarnings("unchecked")
				Map<String, Object> paramMap = (Map<String, Object>) parameter;
				if (paramMap.containsKey(SHADOW_DATABASE)) {
					try {
						shadowDataBase = Integer.valueOf(paramMap.get(SHADOW_DATABASE).toString());
					} catch (Exception e) {
						logger.info("shadowDataBase 数据转换异常");
					}
				}
			} else {
				if (!isBasicType(parameter.getClass())) {
					try {
						shadowDataBase = Integer
								.valueOf(String.valueOf(getFieldValueByName(SHADOW_DATABASE, parameter)));
					} catch (Exception e) {
						logger.debug("没有分页shadowDataBase信息");
					}
				}
			}
		}

		if (1 == shadowDataBase) {
			MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
			DatabaseMetaData metadata = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData();
			ResultSet  schemaResultSet = metadata.getSchemas();
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			String sql = boundSql.getSql();
			sql = "/*!mycat:schema=TESTDB_img*/ " + sql;
			StaticSqlSource staticSqlSource = new StaticSqlSource(mappedStatement.getConfiguration(), sql,boundSql.getParameterMappings());
			if(args.length>=5) {
				BoundSql argBoundSql  = (BoundSql)args[5];
				String attachSql = "/*!mycat:schema=TESTDB_img*/ "+argBoundSql.getSql();
				StaticSqlSource attachSqlSource = new StaticSqlSource(mappedStatement.getConfiguration(), attachSql,boundSql.getParameterMappings());
				args[5] = attachSqlSource.getBoundSql(parameter);
			}
			// 重新new一个查询语句对像
	        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
	        // 把新的查询放到statement里
	        MappedStatement newMs = copyFromMappedStatement(mappedStatement, staticSqlSource);
	        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
	            String prop = mapping.getProperty();
	            if (boundSql.hasAdditionalParameter(prop)) {
	                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
	            }
	        }
//	        args[MAPPED_STATEMENT_INDEX] = null;
	        args[MAPPED_STATEMENT_INDEX] = newMs;
//			StatementHandler stmtHander = (StatementHandler)invocation.getTarget();
//			MetaObject metaStmtHander = SystemMetaObject.forObject(stmtHander);
//			while(metaStmtHander.hasGetter("h")) {
//				Object ob = metaStmtHander.getValue("h");
//				metaStmtHander = SystemMetaObject.forObject(ob);
//			}
//			while(metaStmtHander.hasGetter("target")) {
//				Object ob = metaStmtHander.getValue("target");
//				metaStmtHander = SystemMetaObject.forObject(ob);
//			}
//			String sql = (String)metaStmtHander.getValue("delegate.boundSql.sql");
//			sql = "/*!mycat:schema=TESTDB_img*/ " + sql;
//			if (methodName.equals("query")||methodName.equals("update")) {
//				metaStmtHander.setValue("delegate.boundSql.sql", sql);
//			} 
		}

		return invocation.proceed();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;

	}

	private boolean isBasicType(Class<?> clazz) {
		return clazz.isPrimitive() || clazz == String.class || clazz == Byte.class || clazz == Integer.class
				|| clazz == Double.class || clazz == Float.class || clazz == Boolean.class || clazz == Long.class
				|| clazz == BigDecimal.class || clazz == Character.class || clazz == Short.class
				|| clazz == BigInteger.class || clazz == Date.class || clazz == Object.class;
	}

	/**
	 * @param fieldName
	 * @param o
	 * @return
	 */
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
 
    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}