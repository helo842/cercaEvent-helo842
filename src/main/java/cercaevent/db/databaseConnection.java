package cercaevent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
    private static final String URL = "jdbc:h2:./java/data/eventfinder_db;AUTO_SERVER=TRUE";

    public static Connection getConnection() throws SQLException {
        // Retorno una connexió JDBC a la base de dades H2 local.
        // L'URL apunta al fitxer `java/data/eventfinder_db.*` dins del projecte.
        // Usuari per defecte: sa, sense contrasenya (configuració d'examen/demo).
        return DriverManager.getConnection(URL, "sa", "");

    }

    public static void init() {
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

}
