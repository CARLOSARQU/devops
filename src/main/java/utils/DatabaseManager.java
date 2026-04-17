package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gestiona la conexión al SQL Server del corebank para preparación de datos de prueba.
 *
 * Requiere conexión a red interna o VPN.
 * Activar con db.enabled=true en el archivo .properties del entorno.
 *
 * Solo cambia db.name entre entornos (dev/qa/preprod).
 * El resto de la configuración se completa en el .properties correspondiente.
 */
public class DatabaseManager {

    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private static HikariDataSource dataSource;

    // ──────────────────────────────────────────────────────────────────────────
    // Ciclo de vida — llamados desde Hooks @BeforeAll / @AfterAll
    // ──────────────────────────────────────────────────────────────────────────

    public static void init() {
        if (!isDbEnabled()) {
            log.info("[DB] db.enabled=false — conexión a base de datos omitida.");
            return;
        }

        try {
            String host     = ConfigReader.getProperty("db.host");
            String port     = ConfigReader.getProperty("db.port");
            String name     = ConfigReader.getProperty("db.name");
            String username = ConfigReader.getProperty("db.username");
            String password = ConfigReader.getProperty("db.password");

            String jdbcUrl = String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                host, port, name
            );

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(10_000);
            config.setPoolName("QA-DB-Pool");

            dataSource = new HikariDataSource(config);
            log.info("[DB] Conexión establecida → {}:{}/{}", host, port, name);

        } catch (Exception e) {
            log.error("[DB] No se pudo conectar a la base de datos: {}", e.getMessage());
            log.warn("[DB] Los tests que requieren BD fallarán. Verifica VPN/red y credenciales.");
        }
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("[DB] Pool de conexiones cerrado.");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Estado
    // ──────────────────────────────────────────────────────────────────────────

    public static boolean isAvailable() {
        return dataSource != null && !dataSource.isClosed();
    }

    private static boolean isDbEnabled() {
        String enabled = ConfigReader.getProperty("db.enabled");
        return "true".equalsIgnoreCase(enabled);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Acceso a conexión
    // ──────────────────────────────────────────────────────────────────────────

    public static Connection getConnection() throws SQLException {
        if (!isAvailable()) {
            throw new IllegalStateException(
                "[DB] La conexión no está disponible. Verifica db.enabled=true y VPN."
            );
        }
        return dataSource.getConnection();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers de ejecución — uso interno de métodos de dominio
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Ejecuta una query SELECT y retorna el ResultSet.
     * El caller es responsable de cerrar la conexión y el ResultSet.
     *
     * Uso:
     *   try (Connection conn = DatabaseManager.getConnection();
     *        ResultSet rs = DatabaseManager.query(conn, sql, params)) {
     *       while (rs.next()) { ... }
     *   }
     */
    public static ResultSet query(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        bindParams(stmt, params);
        log.debug("[DB] SELECT → {}", sql);
        return stmt.executeQuery();
    }

    /**
     * Ejecuta un INSERT, UPDATE o DELETE.
     * Retorna el número de filas afectadas.
     *
     * Uso:
     *   try (Connection conn = DatabaseManager.getConnection()) {
     *       int rows = DatabaseManager.update(conn, sql, params);
     *   }
     */
    public static int update(Connection conn, String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindParams(stmt, params);
            log.debug("[DB] UPDATE/INSERT/DELETE → {}", sql);
            return stmt.executeUpdate();
        }
    }

    private static void bindParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO: métodos de dominio — implementar cuando se requiera
    // ──────────────────────────────────────────────────────────────────────────

    /*
     * EJEMPLOS de lo que irá aquí cuando se necesite:
     *
     * public static void setAccountBalance(String accountNumber, double amount) {
     *     String sql = "UPDATE [dbo].[ACCOUNTS] SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";
     *     try (Connection conn = getConnection()) {
     *         int rows = update(conn, sql, amount, accountNumber);
     *         log.info("[DB] Saldo actualizado: cuenta={} saldo={} (filas={})", accountNumber, amount, rows);
     *     } catch (SQLException e) {
     *         throw new RuntimeException("[DB] Error al actualizar saldo: " + e.getMessage());
     *     }
     * }
     *
     * public static double getAccountBalance(String accountNumber) {
     *     String sql = "SELECT BALANCE FROM [dbo].[ACCOUNTS] WHERE ACCOUNT_NUMBER = ?";
     *     try (Connection conn = getConnection();
     *          ResultSet rs = query(conn, sql, accountNumber)) {
     *         if (rs.next()) return rs.getDouble("BALANCE");
     *         throw new RuntimeException("[DB] Cuenta no encontrada: " + accountNumber);
     *     } catch (SQLException e) {
     *         throw new RuntimeException("[DB] Error al consultar saldo: " + e.getMessage());
     *     }
     * }
     *
     * public static void resetTransactionHistory(String accountNumber) { ... }
     * public static String getLastTransactionStatus(String accountNumber) { ... }
     * public static void setCreditStatus(String creditId, String status) { ... }
     */
}
