package main.config;

public class JDBCConfiguration {
    /**
     * JDBC 驱动器
     */
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * mysql 服务端口
     */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * 数据库用户名
     */
    public static final String USER = "your_name";

    /**
     * 数据库密码
     */
    public static final String PASS = "your_password";

    /**
     * 用户数据表
     */
    public static final String USER_TABLE = "your_table_name_for_user";

    /**
     * 文档数据表
     */
    public static final String DOCUMENT_TABLE = "your_table_name_for_document";
}
