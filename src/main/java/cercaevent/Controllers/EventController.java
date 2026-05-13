package cercaevent.Controllers;

import cercaevent.model.Event;
import cercaevent.model.Usuari;
import cercaevent.serveis.App;
import cercaevent.serveis.serveiUsuari;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EventController {

    @FXML
    private TextField titol;
    @FXML
    private TextField ubicacio;
    @FXML
    private TextField data_event;
    @FXML
    private TextField hora_event;
    @FXML
    private TextField aforament;
    @FXML
    private TextField places_disponibles;

    @FXML
    private ComboBox<String> selector;
    @FXML
    private Text type1, type2, type3;
    @FXML
    private TextField password211, password212, password213;

    private static Event eventSeleccionat = null;

    public static void setEventSeleccionat(Event event) {
        eventSeleccionat = event;
    }

    @FXML
    public void initialize() {
        selector.getItems().addAll("Esport", "Videojoc", "Trobada");

        if (eventSeleccionat != null) {
            titol.setText(eventSeleccionat.getTitol());
            ubicacio.setText(eventSeleccionat.getUbicacio());
            data_event.setText(
                    eventSeleccionat.getData_event() != null ? eventSeleccionat.getData_event().toString() : "");
            hora_event.setText(
                    eventSeleccionat.getHora_event() != null ? eventSeleccionat.getHora_event().toString() : "");
            aforament.setText(String.valueOf(eventSeleccionat.getAforament()));
            places_disponibles.setText(String.valueOf(eventSeleccionat.getPlaces_disponibles()));

            selector.setValue(eventSeleccionat.getCategoria());
            actualitzarEtiquetes();

            password211.setText(eventSeleccionat.getCamp1());
            password212.setText(eventSeleccionat.getCamp2());
            password213.setText(eventSeleccionat.getCamp3());
        }
    }

    @FXML
    private void actualitzarEtiquetes() {
        String cat = selector.getValue();
        if ("Esport".equals(cat)) {
            type1.setText("Esport:");
            type2.setText("Nivell:");
            type3.setText("Material:");
        } else if ("Videojoc".equals(cat)) {
            type1.setText("Joc:");
            type2.setText("Plataforma:");
            type3.setText("Modalitat:");
        } else {
            type1.setText("Tema:");
            type2.setText("Tipus:");
            type3.setText("Edat Mín:");
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
        if (u == null) {
            throw new Exception("Error: Sessió nul·la.");
        }
        ev.setCreador_id(u.getId());

        return ev;
    }

    @FXML
    private void hacerEvento() {
        try {
            Event ev = poblarEventDesdeVista();
            serveiUsuari.registrarNuevoEvento(ev);
            tornar();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminar() {
        try {
            if (eventSeleccionat != null) {
                serveiUsuari.eliminarEvento(eventSeleccionat.getId());
                tornar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tornar() {
        try {
            eventSeleccionat = null;
            App.setRoot("CercaEvent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}