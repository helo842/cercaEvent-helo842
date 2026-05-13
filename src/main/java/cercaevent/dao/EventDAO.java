package cercaevent.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import cercaevent.db.databaseConnection;
import cercaevent.model.Event;
import cercaevent.model.Usuari;

public class EventDAO {
    // DAO per a operacions sobre events: creació, modificació, esborrat i
    // consultes.
    // Observacions: alguns mètodes fan checks de permisos (ADMIN vs creador) i
    // retornen boolean per indicar si l'operació s'ha executat.

    public static boolean RegistrarEvent(Event e) throws SQLException {

        String col1 = "tipus_esport", col2 = "nivell", col3 = "material_necessari";
        if ("Videojoc".equals(e.getCategoria())) {
            col1 = "joc";
            col2 = "plataforma";
            col3 = "modalitat";
        } else if ("Trobada".equals(e.getCategoria())) {
            col1 = "tema";
            col2 = "tipus_trobada";
            col3 = "edat_minima";
        }
        String sql = "INSERT INTO events (titol, ubicacio, data_event, hora_event, aforament, places_disponibles, categoria, creador_id, "
                + col1 + ", " + col2 + ", " + col3 + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getTitol());
            ps.setString(2, e.getUbicacio());
            ps.setObject(3, e.getData_event());
            ps.setObject(4, e.getHora_event());

            ps.setInt(5, e.getAforament());
            ps.setInt(6, e.getPlaces_disponibles());
            ps.setString(7, e.getCategoria());
            ps.setInt(8, e.getCreador_id());
            ps.setString(9, e.getCamp1());
            ps.setString(10, e.getCamp2());
            ps.setString(11, e.getCamp3());

            return ps.executeUpdate() > 0;
        }
    }

    public static boolean comprobarEvent(String titol, String ubicacio, String data_event, String hora_event)
            throws SQLException {
        String sql = "SELECT id FROM events WHERE titol = ? AND ubicacio = ? AND data_event = ? AND hora_event = ?";
        System.out.println("comprobarEvent: " + titol + ", " + ubicacio + ", " + data_event + ", " + hora_event);
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titol);
            ps.setString(2, ubicacio);
            ps.setDate(3, Date.valueOf(data_event));
            ps.setTime(4, Time.valueOf(hora_event));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static boolean eliminarEvent(int id, Usuari u) throws SQLException {
        String sql = "ADMIN".equals(u.getRol()) ? "DELETE FROM events WHERE id = ?"
                : "DELETE FROM events WHERE id = ? AND creador_id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (!"ADMIN".equals(u.getRol())) {
                ps.setInt(2, u.getId());
            }

            return ps.executeUpdate() > 0;
        }
    }

    public static boolean editarEvent(Event e, Usuari u) throws SQLException {
        String col1 = "tipus_esport", col2 = "nivell", col3 = "material_necessari";
        if ("Videojoc".equals(e.getCategoria())) {
            col1 = "joc";
            col2 = "plataforma";
            col3 = "modalitat";
        } else if ("Trobada".equals(e.getCategoria())) {
            col1 = "tema";
            col2 = "tipus_trobada";
            col3 = "edat_minima";
        }

        String baseSql = "UPDATE events SET titol=?, ubicacio=?, data_event=?, hora_event=?, aforament=?, places_disponibles=?, categoria=?, "
                + col1 + "=?, " + col2 + "=?, " + col3 + "=? WHERE id=?";

        String sql = "ADMIN".equals(u.getRol()) ? baseSql : baseSql + " AND creador_id=?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTitol());
            ps.setString(2, e.getUbicacio());
            ps.setObject(3, e.getData_event());
            ps.setObject(4, e.getHora_event());
            ps.setInt(5, e.getAforament());
            ps.setInt(6, e.getPlaces_disponibles());
            ps.setString(7, e.getCategoria());
            ps.setString(8, e.getCamp1());
            ps.setString(9, e.getCamp2());
            ps.setString(10, e.getCamp3());
            ps.setInt(11, e.getId());

            if (!"ADMIN".equals(u.getRol())) {
                ps.setInt(12, e.getCreador_id());
            }

            return ps.executeUpdate() > 0;
        }
    }

    public static List<Event> obtenirTotsElsEvents() throws SQLException {
        String sql = "SELECT * FROM events";
        return executarConsultaEvents(sql, null);
    }

    public static List<Event> obtenirElsMeusEvents(int usuariId) throws SQLException {
        String sql = "SELECT * FROM events WHERE creador_id = ?";
        return executarConsultaEvents(sql, usuariId);
    }

    public static List<Event> obtenirEventsInscrits(int usuariId) throws SQLException {
        String sql = "SELECT e.* FROM events e JOIN inscripcions i ON e.id = i.event_id WHERE i.usuari_id = ?";
        return executarConsultaEvents(sql, usuariId);
    }

    private static List<Event> executarConsultaEvents(String sql, Integer parametroId) throws SQLException {
        List<Event> llista = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametroId != null) {
                ps.setInt(1, parametroId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setTitol(rs.getString("titol"));
                e.setUbicacio(rs.getString("ubicacio"));
                e.setData_event(rs.getDate("data_event").toLocalDate());
                e.setHora_event(rs.getTime("hora_event").toLocalTime());
                e.setAforament(rs.getInt("aforament"));
                e.setPlaces_disponibles(rs.getInt("places_disponibles"));

                String categoria = rs.getString("categoria");
                e.setCategoria(categoria);
                e.setCreador_id(rs.getInt("creador_id"));

                if ("Videojoc".equals(categoria)) {
                    e.setCamp1(rs.getString("joc"));
                    e.setCamp2(rs.getString("plataforma"));
                    e.setCamp3(rs.getString("modalitat"));
                } else if ("Trobada".equals(categoria)) {
                    e.setCamp1(rs.getString("tema"));
                    e.setCamp2(rs.getString("tipus_trobada"));
                    e.setCamp3(rs.getString("edat_minima"));
                } else {
                    e.setCamp1(rs.getString("tipus_esport"));
                    e.setCamp2(rs.getString("nivell"));
                    e.setCamp3(rs.getString("material_necessari"));
                }

                llista.add(e);
            }
        }
        return llista;
    }
}