module al9ani {
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


    exports controllers.user;
    opens controllers.user to javafx.fxml;

}