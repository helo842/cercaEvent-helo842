package cercaevent.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int id;
    private String titol;
    private String ubicacio;
    private LocalDate data_event;
    private LocalTime hora_event;
    private int aforament;
    private int places_disponibles;
    private String categoria;
    private int creador_id;

    private String descripcio;
    private String camp1;
    private String camp2;
    private String camp3;

    public Event(int id, String titol, String ubicacio, LocalDate data_event, LocalTime hora_event, int aforament,
            int places_disponibles, String categoria, int creador_id) {
        this.id = id;
        this.titol = titol;
        this.ubicacio = ubicacio;
        this.data_event = data_event;
        this.hora_event = hora_event;
        this.aforament = aforament;
        this.places_disponibles = places_disponibles;
        this.categoria = categoria;
        this.creador_id = creador_id;
    }

    public Event() {
    }

    public String getCamp1() {
        return camp1;
    }

    public void setCamp1(String camp1) {
        this.camp1 = camp1;
    }

    public String getCamp2() {
        return camp2;
    }

    public void setCamp2(String camp2) {
        this.camp2 = camp2;
    }

    public String getCamp3() {
        return camp3;
    }

    public void setCamp3(String camp3) {
        this.camp3 = camp3;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(String ubicacio) {
        this.ubicacio = ubicacio;
    }

    public LocalDate getData_event() {
        return data_event;
    }

    public void setData_event(LocalDate data_event) {
        this.data_event = data_event;
    }

    public LocalTime getHora_event() {
        return hora_event;
    }

    public void setHora_event(LocalTime hora_event) {
        this.hora_event = hora_event;
    }

    public int getAforament() {
        return aforament;
    }

    public void setAforament(int aforament) {
        this.aforament = aforament;
    }

    public int getPlaces_disponibles() {
        return places_disponibles;
    }

    public void setPlaces_disponibles(int places_disponibles) {
        this.places_disponibles = places_disponibles;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCreador_id() {
        return creador_id;
    }

    public void setCreador_id(int creador_id) {
        this.creador_id = creador_id;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", titol=" + titol + ", ubicacio=" + ubicacio + ", data_event=" + data_event
                + ", hora_event=" + hora_event + ", aforament=" + aforament + ", places_disponibles="
                + places_disponibles + ", categoria=" + categoria + ", creador_id=" + creador_id 
                + ", descripcio=" + descripcio + ", camp1=" + camp1 + ", camp2=" + camp2 + ", camp3=" + camp3 + "]";
    }
}