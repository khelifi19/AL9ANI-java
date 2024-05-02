module al9ani {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.prefs;
    requires java.datatransfer;


    exports controllers.user;
    opens controllers.user to javafx.fxml;

}