
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