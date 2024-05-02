module com.example.al9ani {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.prefs;


    opens com.example.al9ani to javafx.fxml;
    exports com.example.al9ani;

    exports controller.user;
    opens controller.user to javafx.fxml;

}