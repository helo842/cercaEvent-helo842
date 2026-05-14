
/*
 * Mòdul: cercaevent
 * Descripció: definició del mòdul per al projecte CercaEvent.
 * Aquest fitxer declara els paquets exposats per la compilació modular.
 */
module cercaevent {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires jbcrypt;
    
        opens cercaevent.Controllers to javafx.fxml;
        opens cercaevent.serveis to javafx.fxml;

        exports cercaevent.Controllers;
        exports cercaevent.serveis;
        exports cercaevent.model;
        exports cercaevent.dao;

}