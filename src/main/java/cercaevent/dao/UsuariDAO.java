package cercaevent.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import cercaevent.db.databaseConnection;
import cercaevent.model.Usuari;

public class UsuariDAO {

    public static Usuari buscarUsuari(String usuari, String password) throws SQLException {
        // Busquem l'usuari per nom d'usuari i comprovem la contrasenya
        // encriptada amb BCrypt. Si coincideix, retornem l'objecte Usuari.
        String sql = "SELECT * FROM usuaris WHERE (usuari = ?)";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuari);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (BCrypt.checkpw(password, rs.getString("password_Hash"))) {
                    Usuari u = new Usuari();
                    u.setId(rs.getInt("id"));
                    u.setUsuari(rs.getString("usuari"));
                    u.setNom(rs.getString("nom"));
                    u.setCognoms(rs.getString("cognoms"));
                    u.setEmail(rs.getString("email"));
                    u.setRol(rs.getString("rol"));
                    u.setDataRegistre(rs.getTimestamp("data_Registre"));
                    u.setPasswordHash(rs.getString("password_Hash"));
                    return u;
                }
            }
        }
        return null;
    }

    // Tasca 5: Actualitzat per incloure el camp ROL en la sentència SQL
    public static Usuari RegistrarUsuari(String nickname, String nom, String cognoms, String email, String password,
            String rol) throws SQLException {
        // Inserció d'un nou usuari a la BD. Rebem el rol des del servei,
        // no es mostra com a camp del formulari.
        String sql = "INSERT INTO usuaris (usuari, nom, cognoms, email, password_Hash, rol) VALUES (?, ?, ?, ?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nickname);
            ps.setString(2, nom);
            ps.setString(3, cognoms);
            ps.setString(4, email);
            ps.setString(5, hashedPassword);
            ps.setString(6, rol);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                Usuari u = new Usuari();
                u.setUsuari(nickname);
                u.setNom(nom);
                u.setCognoms(cognoms);
                u.setEmail(email);
                u.setPasswordHash(hashedPassword);
                u.setRol(rol);

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        u.setId(generatedKeys.getInt(1));
                    }
                }
                return u;
            }
        }
        return null;
    }

    public static boolean comprobarSiExiste(String usuari, String email) throws SQLException {
        String sql = "SELECT id FROM usuaris WHERE usuari = ? OR email = ?";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuari);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}