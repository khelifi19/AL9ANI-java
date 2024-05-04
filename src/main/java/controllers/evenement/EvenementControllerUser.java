package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import models.Evenement;
import services.ServiceEvenement;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EvenementControllerUser {

    @FXML
    private ListView<Evenement> evenementListView;
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfType;

    @FXML
    private TextField tfParticipants;

    @FXML
    private DatePicker dpDateDebut;

    @FXML
    private DatePicker dpDateFin;

    @FXML
    private void initialize() {
        afficherEvenement();
    }

    @FXML
    private void afficherEvenement() {
        try {
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            List<Evenement> evenements = serviceEvenement.selectAll();

            ObservableList<Evenement> evenementsObservableList = FXCollections.observableArrayList(evenements);

            evenementListView.setItems(evenementsObservableList);

            evenementListView.setCellFactory(eventListView -> new ListCell<Evenement>() {
                @Override
                protected void updateItem(Evenement evenement, boolean empty) {
                    super.updateItem(evenement, empty);
                    if (empty || evenement == null) {
                        setText(null);
                        setGraphic(null); // Clear the graphic when the cell is empty
                    } else {
                        // Display all event details
                        setText("Nom: " + evenement.getNom() + "\n" +
                                "Type: " + evenement.getType() + "\n" +
                                "Participants: " + evenement.getParticipants() + "\n" +
                                "Date début: " + evenement.getDateDebut() + "\n" +
                                "Date fin: " + evenement.getDateFin());

                        // Add QR button
                        Button qrButton = new Button("Plus d'informations");
                        qrButton.setOnAction(event -> generateAndShowQRCode(evenement));
                        setGraphic(qrButton);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch events from the database.");
        }
    }
    @FXML

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    private void generateAndShowQRCode(Evenement evenement) {
        try {
            // Generate the QR code for the event
            byte[] qrCodeImage = QRCodeGenerator.generateQRCode("http://127.0.0.1:8000/eventdetails", evenement.getId());

            // Convert the byte array to an Image
            InputStream inputStream = new ByteArrayInputStream(qrCodeImage);
            Image qrCode = new Image(inputStream);

            // Display the QR code in a dialog
            Dialog<Image> dialog = new Dialog<>();
            dialog.setTitle("QR Code");
            dialog.setHeaderText("QR Code for " + evenement.getNom());
            dialog.setResizable(true);

            // Set the image as dialog content
            dialog.getDialogPane().setContent(new ImageView(qrCode));

            // Add a close button
            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            // Show the dialog
            dialog.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate QR code.");
        }
    }
}

