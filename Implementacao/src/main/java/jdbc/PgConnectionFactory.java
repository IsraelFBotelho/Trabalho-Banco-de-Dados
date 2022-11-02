package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PgConnectionFactory extends ConnectionFactory {
    private String dbHost;
    private String dbPort;
    private String dbDatabase;
    private String dbUser;
    private String dbPassword;

    public PgConnectionFactory() {
    }

    public void readProperties() throws IOException {
        Properties properties = new Properties();

        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(propertiesPath);

            properties.load(input);
            dbHost = properties.getProperty("host");
            dbPort = properties.getProperty("port");
            dbDatabase = properties.getProperty("database");
            dbUser = properties.getProperty("user");
            dbPassword = properties.getProperty("password");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IOException("Falha na conexão ao banco de dados!");
        }
    }

    @Override
    public Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        String className = "org.postgresql.Driver";
        Connection connection = null;

        try {
            Class.forName(className);
            readProperties();

            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbDatabase;
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            throw new ClassNotFoundException("Não foi possível encontrar a classe '" + className + "'!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Falha na conexão ao banco de dados!");
        }

        return connection;
    }
}
