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

    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires ical4j.core;
    requires org.controlsfx.controls;


    exports  modeles.uber;

    opens modeles.evenement to javafx.base;
    exports  modeles.evenement;
opens modeles.uber to javafx.base;
    exports com.example.al9ani.test;
    opens com.example.al9ani.test to javafx.fxml;

    exports controllers.user;
    opens controllers.user to javafx.fxml;
    exports controllers.front;
    opens controllers.front to javafx.fxml;
    exports controllers.back;
    opens controllers.back to javafx.fxml;
    exports controllers.uber;
    opens controllers.uber to javafx.fxml;
    exports controllers.reclamation;
    opens controllers.reclamation to javafx.fxml;
    exports controllers.evenement;
    opens controllers.evenement to javafx.fxml;

    exports controllers.front.post;
    opens controllers.front.post to javafx.fxml;
    exports controllers.front.etablissement;
    opens controllers.front.etablissement to javafx.fxml;
    exports controllers.front.commentaire;
    opens controllers.front.commentaire to javafx.fxml;
    exports controllers.front.reservation;
    opens controllers.front.reservation to javafx.fxml;
    exports controllers.back.commentaire;
    opens controllers.back.commentaire to javafx.fxml;
    exports controllers.back.etablissement;
    opens controllers.back.etablissement to javafx.fxml;
    exports controllers.back.reservation;
    opens controllers.back.reservation to javafx.fxml;
    exports controllers.back.post;
    opens controllers.back.post to javafx.fxml;


}
