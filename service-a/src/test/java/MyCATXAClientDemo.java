import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;


public class MyCATXAClientDemo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	Connection conn = null;
    	try {
    		// 1. 获得数据库连接
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.01:8066/TESTDB?useSSL=false", "root", "rootroot");
            conn.setAutoCommit(false);

            // 2. 开启 MyCAT XA 事务
            conn.prepareStatement("set xa=on").execute();

            // 3. 插入 SQL
            // 3.1 SQL1 A库
            int uid = Math.abs(new Random().nextInt());
            String username = UUID.randomUUID().toString();
            String password = UUID.randomUUID().toString();
            String sql1 = String.format("insert into dist_tran(id, name) VALUES (%d, '%s')",
                    uid, username);
            conn.prepareStatement(sql1).execute();
            // 3.2 SQL2 B库
            int orderId = Math.abs(new Random().nextInt());
            String nickname = UUID.randomUUID().toString();
            orderId = 731281078;
            String sql2 = String.format("insert into dist_tran2(id, name) VALUES(%d, '%s')", orderId, nickname);
            conn.prepareStatement(sql2).execute();

            // 4. 提交 XA 事务
            conn.commit();
            if(null!=conn) {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(null!=conn) {
				conn.rollback();
			}
		}
        
    }

}