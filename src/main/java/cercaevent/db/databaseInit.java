package cercaevent.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class databaseInit {

public static void init() {
        try (
            Connection conn = databaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            InputStream is = databaseInit.class.getResourceAsStream("schema.sql")
        ) {
            if (is == null) {
                throw new RuntimeException("No s'ha trobat schema.sql");
            }

            String sql = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            for (String part : sql.split(";")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inicialitzant la base de dades", e);
        }
    }
}