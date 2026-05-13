package cercaevent.Controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import cercaevent.dao.EventDAO;
import cercaevent.model.Event;
import cercaevent.model.Usuari;
import cercaevent.serveis.App;
import cercaevent.serveis.serveiUsuari;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class CercaEventController {

    @FXML
    private TextField Ubicacio;
    @FXML
    private DatePicker Data;
    @FXML
    private CheckBox Places;
    @FXML
    private Button Aplicar, Netejar, Tots, Meus, Inscrits, btAdeu, btCrearUsuari;
    @FXML
    private Label lblNomUsuari;

    @FXML
    private TableView<Event> tableEvents;
    @FXML
    private TableColumn<Event, String> colTitol;
    @FXML
    private TableColumn<Event, String> colCategoria;
    @FXML
    private TableColumn<Event, String> colUbicacio;
    @FXML
    private TableColumn<Event, LocalDate> colData;
    @FXML
    private TableColumn<Event, LocalTime> colHora;
    @FXML
    private TableColumn<Event, Integer> colPlaces;

    private List<Event> llistaActual = new ArrayList<>();
    private Usuari usuariActual;

    @FXML
    public void initialize() {
        // Inicialització del controlador.
        // - Recuperem l'usuari loguejat per adaptar la UI (nom, permisos)
        // - Configurem les columnes de la taula i carreguem tots els events.
        // Comentaris: faig comprovacions null per evitar excepcions si algun fx:id
        // falta al FXML durant proves.
        usuariActual = serveiUsuari.getUsuariLoguejat();

        // --- ESCUTS ANTI-CRASH ---
        if (usuariActual != null) {
            if (lblNomUsuari != null) {
                lblNomUsuari.setText("Usuari: " + usuariActual.getNom());
            } else {
                System.out.println("⚠️ ALERTA: lblNomUsuari no s'ha trobat al FXML!");
            }

            if ("ADMIN".equals(usuariActual.getRol())) {
                if (btCrearUsuari != null) {
                    btCrearUsuari.setVisible(true);
                    btCrearUsuari.setManaged(true);
                } else {
                    System.out.println("⚠️ ALERTA: btCrearUsuari no s'ha trobat al FXML!");
                }
            }
        }

        // Més seguretat per a les columnes de la taula
        if (colTitol != null)
            colTitol.setCellValueFactory(new PropertyValueFactory<>("titol"));
        if (colCategoria != null)
            colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        if (colUbicacio != null)
            colUbicacio.setCellValueFactory(new PropertyValueFactory<>("ubicacio"));
        if (colData != null)
            colData.setCellValueFactory(new PropertyValueFactory<>("data_event"));
        if (colHora != null)
            colHora.setCellValueFactory(new PropertyValueFactory<>("hora_event"));
        if (colPlaces != null)
            colPlaces.setCellValueFactory(new PropertyValueFactory<>("places_disponibles"));

        carregarTots();
    }

    @FXML
    private void carregarTots() {
        // Carrego tots els events des de la BD i actualitzo l'estat dels botons
        // (Tots/Meus/Inscrits). En cas d'error, ho faig printStackTrace perquè
        // és més senzill de debugar en l'entorn d'examen.
        try {
            llistaActual = EventDAO.obtenirTotsElsEvents();
            if (Tots != null)
                Tots.setDisable(true);
            if (Meus != null)
                Meus.setDisable(false);
            if (Inscrits != null)
                Inscrits.setDisable(false);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarMeus() {
        // Carrego només els events creats pel usuari actual.
        try {
            if (usuariActual == null)
                return;
            llistaActual = EventDAO.obtenirElsMeusEvents(usuariActual.getId());
            if (Tots != null)
                Tots.setDisable(false);
            if (Meus != null)
                Meus.setDisable(true);
            if (Inscrits != null)
                Inscrits.setDisable(false);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarInscrits() {
        // Carrego només els events on l'usuari està inscrit.
        try {
            if (usuariActual == null)
                return;
            llistaActual = EventDAO.obtenirEventsInscrits(usuariActual.getId());
            if (Tots != null)
                Tots.setDisable(false);
            if (Meus != null)
                Meus.setDisable(false);
            if (Inscrits != null)
                Inscrits.setDisable(true);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void aplicarFiltros() {
        // Aplicar filtres simples sobre la llistaActual.
        // Edge cases: controlo camps null per evitar NullPointerException.
        if (Ubicacio == null || Data == null || Places == null)
            return;

        String ubiBusqueda = Ubicacio.getText().toLowerCase();
        LocalDate dataBusqueda = Data.getValue();
        boolean nomasAmbPlaces = Places.isSelected();

        List<Event> llistaFiltrada = new ArrayList<>();

        for (int i = 0; i < llistaActual.size(); i++) {
            Event e = llistaActual.get(i);
            boolean cumpleUbi = true;
            boolean cumpleData = true;
            boolean cumplePlaces = true;

            if (ubiBusqueda.length() > 0) {
                if (e.getUbicacio().toLowerCase().contains(ubiBusqueda) == false) {
                    cumpleUbi = false;
                }
            }

            if (dataBusqueda != null) {
                if (e.getData_event() == null || !e.getData_event().equals(dataBusqueda)) {
                    cumpleData = false;
                }
            }

            if (nomasAmbPlaces == true) {
                if (e.getPlaces_disponibles() <= 0) {
                    cumplePlaces = false;
                }
            }

            if (cumpleUbi && cumpleData && cumplePlaces) {
                llistaFiltrada.add(e);
            }
        }

        ObservableList<Event> observableList = FXCollections.observableArrayList(llistaFiltrada);
        if (tableEvents != null)
            tableEvents.setItems(observableList);
    }

    @FXML
    private void netejarFiltros() {
        if (Ubicacio != null)
            Ubicacio.clear();
        if (Data != null)
            Data.setValue(null);
        if (Places != null)
            Places.setSelected(false);
        aplicarFiltros();
    }

    @FXML
    private void clickTaula(MouseEvent event) {
        if (event.getClickCount() == 1 && tableEvents != null) {
            Event eventSeleccionat = tableEvents.getSelectionModel().getSelectedItem();
            if (eventSeleccionat != null) {
                try {
                    gestionarClicEvent(eventSeleccionat);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void gestionarClicEvent(Event evento) throws IOException {
        if (usuariActual == null)
            return;
        boolean esAdmin = "ADMIN".equals(usuariActual.getRol());
        boolean esPropietario = evento.getCreador_id() == usuariActual.getId();

        // Si som admin o propietari, obrim la vista d'edició/visualització de l'event.
        if (esAdmin || esPropietario) {
            EventController.setEventSeleccionat(evento);
            App.setRoot("Event");
        } else {
            System.out.println("No tens permisos.");
        }
    }

    @FXML
    private void adeuSessio(ActionEvent event) {
        // Netejo la sessió i torno a la pantalla de login.
        // Aquest mètode és cridat pel botó btAdeu.
        serveiUsuari.tancarSessio();
        try {
            App.setRoot("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void crearUsuariAdmin(ActionEvent event) {
        try {
            App.setRoot("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}