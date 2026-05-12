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

        String sql = "SELECT * FROM usuaris WHERE (usuari = ?)";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuari);
            ResultSet rs = ps.executeQuery();

            System.out.println("Cercant usuari: " + usuari);
            System.out.println(sql);

            if (rs.next()) {
                System.out.println("Usuari trobat: " + rs.getString("usuari"));
                System.out.println("Hash de la contrasenya a la BD: " + rs.getString("password_Hash"));
                System.out.println("Contrasenya introduida: " + password);
                System.out.println("Comparació de contrasenyes: "
                        + BCrypt.checkpw(password, rs.getString("password_Hash")));

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
                    System.out.println("usuari generat");
                    return u;
                } else {
                    System.out.println("Contrasenya incorrecta");
                }
            }
        }
        return null; 
    }






public static Usuari RegistrarUsuari(String nickname, String nom, String cognoms, String email, String password) throws SQLException {
    String sql = "INSERT INTO usuaris (usuari, nom, cognoms, email, password_Hash) VALUES (?, ?, ?, ?, ?)";
    
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         
        ps.setString(1, nickname);
        ps.setString(2, nom);
        ps.setString(3, cognoms);
        ps.setString(4, email);
        ps.setString(5, hashedPassword);

        int filasAfectadas = ps.executeUpdate();
        if (filasAfectadas > 0) {
            Usuari u = new Usuari();
            u.setUsuari(nickname);
            u.setNom(nom);
            u.setCognoms(cognoms);
            u.setEmail(email);
            u.setPasswordHash(hashedPassword);

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