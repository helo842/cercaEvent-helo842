/*
 * Servei d'usuari: encapsula la lògica d'alt nivell per gestionar
 * operacions d'usuaris i events (validacions i crides a DAO).
 * Comentaris en català per facilitar manteniment i comprensió.
 */
package cercaevent.serveis;

import cercaevent.dao.UsuariDAO;
import cercaevent.model.Event;
import cercaevent.model.Usuari;

public class serveiUsuari {
    private static Usuari usuariLoguejat;

    public static Usuari getUsuariLoguejat() {
        return usuariLoguejat;
    }

    public static boolean existeixUsuari(String uUsuari, String contrasenya) throws Exception {
        System.out.println(contrasenya + ", " + uUsuari);
        Usuari usuari = UsuariDAO.buscarUsuari(uUsuari, contrasenya);
        
        if (usuari == null){
             throw new Exception("Usuari no existeix o contrasenya incorrecta");
        }
        usuariLoguejat = usuari;
        
        return true;
    }

    public static void registrarNuevoUsuario(String uUsuari, String uNom, String uCognoms, String uEmail, String uContrasenya) throws Exception {
        boolean existe = UsuariDAO.comprobarSiExiste(uUsuari, uEmail);
        if (existe) {
            throw new Exception("L'usuari o l'email ja estan registrats.");
        }
        
        Usuari usuariNou = UsuariDAO.RegistrarUsuari(uUsuari, uNom, uCognoms, uEmail, uContrasenya);
        
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
        if(!eliminat) {
            throw new Exception("No pots eliminar aquest event o no existeix.");
        }
    }
    
    public static void editarEvento(Event event) throws java.sql.SQLException, Exception {
        Usuari u = getUsuariLoguejat();
        boolean editat = cercaevent.dao.EventDAO.editarEvent(event, u);
        if(!editat) {
            throw new Exception("No s'ha pogut editar. L'ID no existeix o no tens permisos.");
        }
    }
}