/*
 * Controlador de la vista d'Event: encapsula la interacció amb la UI
 * per crear, editar, eliminar i gestionar inscripcions d'un event.
 */
package cercaevent.Controllers;

import java.util.List;

import cercaevent.dao.EventDAO;
import cercaevent.model.Event;
import cercaevent.model.Usuari;
import cercaevent.serveis.App;
import cercaevent.serveis.serveiUsuari;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EventController {

    @FXML private TextField titol;
    @FXML private TextField ubicacio;
    @FXML private TextField data_event;
    @FXML private TextField hora_event;
    @FXML private TextField aforament;
    @FXML private TextField places_disponibles;
    
    @FXML private ComboBox<String> selector;
    @FXML private Text type1, type2, type3; 
    @FXML private TextField password211, password212, password213;

    // Referències FXML als botons de la interfície: crear, actualitzar, eliminar i gestió d'inscripcions
    @FXML private Button btnCreate, btnUpdate, btnDelete, btnInscriure, btnBaixa, btnInscrits;
    
    private static Event eventSeleccionat = null;

    public static void setEventSeleccionat(Event event) {
        eventSeleccionat = event;
    }

    @FXML
    public void initialize() {
        selector.getItems().addAll("Esport", "Videojoc", "Trobada");
        Usuari u = serveiUsuari.getUsuariLoguejat();

        if (eventSeleccionat != null) {
            titol.setText(eventSeleccionat.getTitol());
            ubicacio.setText(eventSeleccionat.getUbicacio());
            data_event.setText(eventSeleccionat.getData_event() != null ? eventSeleccionat.getData_event().toString() : "");
            hora_event.setText(eventSeleccionat.getHora_event() != null ? eventSeleccionat.getHora_event().toString() : "");
            aforament.setText(String.valueOf(eventSeleccionat.getAforament()));
            places_disponibles.setText(String.valueOf(eventSeleccionat.getPlaces_disponibles()));
            
            selector.setValue(eventSeleccionat.getCategoria());
            actualitzarEtiquetes(); 
            
            password211.setText(eventSeleccionat.getCamp1());
            password212.setText(eventSeleccionat.getCamp2());
            password213.setText(eventSeleccionat.getCamp3());

            // Gestió de permisos: comprovar rol/creador i ajustar visibilitat i edició dels controls
            boolean isCreador = (eventSeleccionat.getCreador_id() == u.getId());
            boolean isAdmin = "ADMIN".equals(u.getRol());

            if (isAdmin || isCreador) {
                // Administrador o creador: accés complet als controls (editar, eliminar, veure inscrits)
                mostrarBoto(btnCreate, false);
                mostrarBoto(btnUpdate, true);
                mostrarBoto(btnDelete, true);
                mostrarBoto(btnInscrits, true);
                mostrarBoto(btnInscriure, false);
                mostrarBoto(btnBaixa, false);
            } else {
                // Usuari normal: vista en mode lectura i opcions d'inscripció/baixa segons disponibilitat
                titol.setEditable(false);
                ubicacio.setEditable(false);
                data_event.setEditable(false);
                hora_event.setEditable(false);
                aforament.setEditable(false);
                places_disponibles.setEditable(false);
                selector.setDisable(true);
                password211.setEditable(false);
                password212.setEditable(false);
                password213.setEditable(false);

                mostrarBoto(btnCreate, false);
                mostrarBoto(btnUpdate, false);
                mostrarBoto(btnDelete, false);
                mostrarBoto(btnInscrits, false);

                actualitzarBotonsInscripcio(u);
            }
        } else {
            // Mode creació: configurar la vista per introduir un nou event
            mostrarBoto(btnCreate, true);
            mostrarBoto(btnUpdate, false);
            mostrarBoto(btnDelete, false);
            mostrarBoto(btnInscrits, false);
            mostrarBoto(btnInscriure, false);
            mostrarBoto(btnBaixa, false);
        }
    }

    private void mostrarBoto(Button btn, boolean visible) {
        if(btn != null) {
            btn.setVisible(visible);
            btn.setManaged(visible);
        }
    }

    private void actualitzarBotonsInscripcio(Usuari u) {
        try {
            boolean inscrit = EventDAO.estaInscrit(u.getId(), eventSeleccionat.getId());
            if (inscrit) {
                mostrarBoto(btnInscriure, false);
                mostrarBoto(btnBaixa, true);
            } else {
                mostrarBoto(btnInscriure, true);
                mostrarBoto(btnBaixa, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualitzarEtiquetes() {
        String cat = selector.getValue();
        if ("Esport".equals(cat)) {
            type1.setText("Tipus:"); type2.setText("Nivell:"); type3.setText("Material:");
        } else if ("Videojoc".equals(cat)) {
            type1.setText("Joc:"); type2.setText("Plataforma:"); type3.setText("Modalitat:");
        } else {
            type1.setText("Tema:"); type2.setText("Tipus:"); type3.setText("Edat Mín:");
        }
    }

    private Event poblarEventDesdeVista() throws Exception {
        Event ev = new Event();
        ev.setTitol(titol.getText());
        ev.setUbicacio(ubicacio.getText());
        ev.setData_event(java.time.LocalDate.parse(data_event.getText()));
        ev.setHora_event(java.time.LocalTime.parse(hora_event.getText()));
        ev.setAforament(Integer.parseInt(aforament.getText()));
        ev.setPlaces_disponibles(Integer.parseInt(places_disponibles.getText()));
        ev.setCategoria(selector.getValue());
        
        ev.setCamp1(password211.getText());
        ev.setCamp2(password212.getText());
        ev.setCamp3(password213.getText());
        
        Usuari u = serveiUsuari.getUsuariLoguejat();
        if(u == null) { throw new Exception("Error: Sessió nul·la."); }
        ev.setCreador_id(u.getId());
        
        return ev;
    }

    @FXML
    private void hacerEvento() {
        try {
            Event ev = poblarEventDesdeVista();
            serveiUsuari.registrarNuevoEvento(ev);
            tornar();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @FXML
    private void editarEvento() {
        try {
            if (eventSeleccionat != null) {
                Event ev = poblarEventDesdeVista();
                ev.setId(eventSeleccionat.getId()); 
                serveiUsuari.editarEvento(ev);
                tornar();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void eliminar() {
        try {
            if (eventSeleccionat != null){
                 serveiUsuari.eliminarEvento(eventSeleccionat.getId());
                 tornar();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Accions relacionades amb les inscripcions: inscriure, donar baixa i llistar inscrits

    @FXML
    private void inscriurem() {
        Usuari u = serveiUsuari.getUsuariLoguejat();
        try {
            if (eventSeleccionat.getPlaces_disponibles() > 0) {
                boolean ok = EventDAO.inscriure(u.getId(), eventSeleccionat.getId());
                if (ok) {
                    eventSeleccionat.setPlaces_disponibles(eventSeleccionat.getPlaces_disponibles() - 1);
                    places_disponibles.setText(String.valueOf(eventSeleccionat.getPlaces_disponibles()));
                    actualitzarBotonsInscripcio(u);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Inscripció confirmada", "T'has inscrit correctament a l'event.");
                } else {
                    mostrarAlerta(Alert.AlertType.WARNING, "Error", "No s'ha pogut realitzar la inscripció.");
                }
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Sense places", "Ho sentim, no queden places disponibles per aquest event.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ja estàs inscrit o hi ha hagut un error a la base de dades.");
        }
    }

    @FXML
    private void donarBaixa() {
        Usuari u = serveiUsuari.getUsuariLoguejat();
        try {
            boolean ok = EventDAO.donarBaixa(u.getId(), eventSeleccionat.getId());
            if (ok) {
                eventSeleccionat.setPlaces_disponibles(eventSeleccionat.getPlaces_disponibles() + 1);
                places_disponibles.setText(String.valueOf(eventSeleccionat.getPlaces_disponibles()));
                actualitzarBotonsInscripcio(u);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Baixa confirmada", "T'has donat de baixa de l'event amb èxit.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void veureInscrits() {
        try {
            List<String> llista = EventDAO.obtenirPersonesInscrites(eventSeleccionat.getId());
            String text = String.join("\n", llista);
            if (text.isEmpty()) text = "Encara no hi ha cap persona inscrita.";
            mostrarAlerta(Alert.AlertType.INFORMATION, "Persones inscrites a " + eventSeleccionat.getTitol(), text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipus, String titol, String missatge) {
        Alert alert = new Alert(tipus);
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(missatge);
        alert.showAndWait();
    }

    @FXML
    private void tornar() {
        try {
           eventSeleccionat = null; 
           App.setRoot("CercaEvent");
        } catch (Exception e) { e.printStackTrace(); }
    }
}