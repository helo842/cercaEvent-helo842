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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class CercaEventController {

    @FXML private TextField Ubicacio;
    @FXML private DatePicker Data;
    @FXML private CheckBox Places;
    @FXML private Button Aplicar, Netejar, Tots, Meus, Inscrits;

    @FXML private TableView<Event> tableEvents;
    @FXML private TableColumn<Event, String> colTitol;
    @FXML private TableColumn<Event, String> colCategoria;
    @FXML private TableColumn<Event, String> colUbicacio;
    @FXML private TableColumn<Event, LocalDate> colData;
    @FXML private TableColumn<Event, LocalTime> colHora;
    @FXML private TableColumn<Event, Integer> colPlaces;

    private List<Event> llistaActual = new ArrayList<>();
    private Usuari usuariActual;

    @FXML
    public void initialize () {
        usuariActual = serveiUsuari.getUsuariLoguejat();
        colTitol.setCellValueFactory(new PropertyValueFactory<>("titol"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUbicacio.setCellValueFactory(new PropertyValueFactory<>("ubicacio"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data_event"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora_event"));
        colPlaces.setCellValueFactory(new PropertyValueFactory<>("places_disponibles"));
        carregarTots();
    }

    @FXML
    private void carregarTots() {
        try {
            llistaActual = EventDAO.obtenirTotsElsEvents();
            Tots.setDisable(true); 
            Meus.setDisable(false); 
            Inscrits.setDisable(false);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarMeus() {
        try {
            if (usuariActual == null) return; 
            llistaActual = EventDAO.obtenirElsMeusEvents(usuariActual.getId());
            Tots.setDisable(false); 
            Meus.setDisable(true); 
            Inscrits.setDisable(false);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarInscrits() {
        try {
            if (usuariActual == null) return; 
            llistaActual = EventDAO.obtenirEventsInscrits(usuariActual.getId());
            Tots.setDisable(false); 
            Meus.setDisable(false); 
            Inscrits.setDisable(true);
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void aplicarFiltros() {
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
        tableEvents.setItems(observableList);
    }

    @FXML
    private void netejarFiltros() {
        Ubicacio.clear();
        Data.setValue(null);
        Places.setSelected(false);
        aplicarFiltros(); 
    }

    @FXML
    private void clickTaula(MouseEvent event) {
        if (event.getClickCount() == 1) {
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
        if (usuariActual == null) {
            System.out.println("Atenció: No hi ha cap usuari loguejat (sessió nula).");
            return;
        }
    boolean esAdmin = "ADMIN".equals(usuariActual.getRol());
        boolean esPropietario = evento.getCreador_id() == usuariActual.getId();

        if (esAdmin || esPropietario) {
            EventController.setEventSeleccionat(evento); 
            App.setRoot("Event");
        } else {
            System.out.println("No tens els permisos d'ADMINISTRADOR ni ets el creador per veure/editar aquest event.");
        }
    }
}