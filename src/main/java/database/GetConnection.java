package database;

import static utils.TryCatch.*;

import com.zaxxer.hikari.*;
import java.sql.*;

/**
 * This class get connection to the SQLite database using JDBC Driver
 */
public class GetConnection {

  public interface SQLConsumer {
    void accept(Connection c) throws SQLException;
  }

  public interface SQLFunction<T> {
    T apply(Connection conn) throws SQLException;
  }

  private static HikariDataSource dataSource;

  private static String getEnvDefault(String name, String default_value) {
    String value = System.getenv(name);
    if (value == null) {
      return default_value;
    } else
      return value;
  }

  public static void withConnection(SQLConsumer f) {
    try (Connection conn = getConnection()) {
      f.accept(conn);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T withConnectionReturning(SQLFunction<T> f) {
    try (Connection conn = getConnection()) {
      return f.apply(conn);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void initIfNecessary() {
    if (dataSource == null) {
      HikariConfig config = new HikariConfig();
      config.setUsername(getEnvDefault("DB_USERNAME", "schedge"));
      config.setPassword(getEnvDefault("DB_PASSWORD", ""));
      config.setJdbcUrl(getEnvDefault(
          "JDBC_URL", "jdbc:postgresql://127.0.0.1:5432/schedge"));

      // We retry a few times so that schedge doesn't hard-crash when running in
      // docker-compose
      for (int i = 0; i < 10; i++) {
        dataSource = tcIgnore(() -> new HikariDataSource(config));
        if (dataSource != null)
          return;

        tcIgnore(() -> Thread.sleep(3000));
      }

      dataSource = new HikariDataSource(config);
    }
  }

  public static Connection getConnection() throws SQLException {
    initIfNecessary();
    return dataSource.getConnection();
  }

  public static void close() {
    tcFatal(() -> {
      if (dataSource != null)
        dataSource.close();
      dataSource = null;
    });
  }
}
