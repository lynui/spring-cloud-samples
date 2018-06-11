package self.spring.cloud.sample.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

@Configuration
public class DruidDBConfig {
    private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);
    @Autowired
    WallFilter wallFilter;
    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();

        List<Filter> filters = new ArrayList<Filter>();
        filters.add((Filter)wallFilter);
        datasource.setProxyFilters(filters);

        return datasource;
    }

    @Bean(name = "wallConfig")
    WallConfig wallFilterConfig(){
        WallConfig wc = new WallConfig ();
        wc.setMultiStatementAllow(true);
        return wc;
    }

    @Bean(name = "wallFilter")
    @DependsOn("wallConfig")
    WallFilter wallFilter(WallConfig wallConfig){
        WallFilter wfilter = new WallFilter ();
        wfilter.setConfig(wallConfig);
        return wfilter;
    }
}