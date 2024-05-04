module com.example.al9ani {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.prefs;
    requires java.datatransfer;
    requires twilio;
    requires stripe.java;
    requires javafx.web;
    requires java.mail;
    requires mysql.connector.j;
    requires javafx.graphics;
    requires java.desktop;
    requires itextpdf;

exports  modeles.uber;
opens modeles.uber to javafx.base;
    exports com.example.al9ani.test;
opens com.example.al9ani.test to javafx.fxml;
    exports controllers.user;
    opens controllers.user to javafx.fxml;
    exports controllers.uber;
    opens controllers.uber to javafx.fxml;
    exports controllers.reclamation;
    opens controllers.reclamation to javafx.fxml;

}