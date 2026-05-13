package cercaevent.serveis;

import cercaevent.dao.UsuariDAO;
import cercaevent.model.Event;
import cercaevent.model.Usuari;

public class serveiUsuari {
    private static Usuari usuariLoguejat;

    public static Usuari getUsuariLoguejat() {
        return usuariLoguejat;
    }

    // Tasca 2: Netejar informació de l'usuari actual
    public static void tancarSessio() {
        // Quan tanquem sessió, elimino la referència a l'usuari actual.
        // Això evita que dades sensibles quedin en memòria i fa que la UI
        // pugui detectar fàcilment que no hi ha sessió activa.
        usuariLoguejat = null;
    }

    public static boolean existeixUsuari(String uUsuari, String contrasenya) throws Exception {
        // Intento buscar l'usuari a la BD i validar la contrasenya.
        // Si és correcte, guardo l'usuari a la sessió (variable estàtica).
        Usuari usuari = UsuariDAO.buscarUsuari(uUsuari, contrasenya);

        if (usuari == null) {
            // Es llança excepció perquè els controllers puguin mostrar missatge a UI.
            throw new Exception("Usuari no existeix o contrasenya incorrecta");
        }
        usuariLoguejat = usuari;

        return true;
    }

    // Tasca 5: Adaptat per rebre el rol
    public static void registrarNuevoUsuario(String uUsuari, String uNom, String uCognoms, String uEmail,
            String uContrasenya, String rol) throws Exception {
        boolean existe = UsuariDAO.comprobarSiExiste(uUsuari, uEmail);
        if (existe) {
            throw new Exception("L'usuari o l'email ja estan registrats.");
        }

        Usuari usuariNou = UsuariDAO.RegistrarUsuari(uUsuari, uNom, uCognoms, uEmail, uContrasenya, rol);

        if (usuariNou == null) {
            throw new Exception("Error desconegut al registrar a la base de dades.");
        }
    }

    public static void registrarNuevoEvento(Event event) throws java.sql.SQLException {
        cercaevent.dao.EventDAO.RegistrarEvent(event);
    }

    public static void eliminarEvento(int id) throws java.sql.SQLException, Exception {
        Usuari u = getUsuariLoguejat();
        boolean eliminat = cercaevent.dao.EventDAO.eliminarEvent(id, u);
        if (!eliminat) {
            throw new Exception("No pots eliminar aquest event o no existeix.");
        }
    }

    public static void editarEvento(Event event) throws java.sql.SQLException, Exception {
        Usuari u = getUsuariLoguejat();
        boolean editat = cercaevent.dao.EventDAO.editarEvent(event, u);
        if (!editat) {
            throw new Exception("No s'ha pogut editar. L'ID no existeix o no tens permisos.");
        }
    }
}