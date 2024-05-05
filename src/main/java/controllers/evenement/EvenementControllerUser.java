package controllers.evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import modeles.evenement.Evenement;
import service.serviceEvenement.ServiceEvenement;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import controllers.IcsGenerator;

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
    private TextField rechercherid;
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
                        setGraphic(null);
                    } else {
                        // Display all event details
                        setText("Nom: " + evenement.getNom() + "\n" +
                                "Type: " + evenement.getType() + "\n" +
                                "Participants: " + evenement.getParticipants() + "\n" +
                                "Date début: " + evenement.getDateDebut() + "\n" +
                                "Date fin: " + evenement.getDateFin());

                        // Create buttons
                        Button qrButton = new Button("Plus d'informations");
                        qrButton.setOnAction(event -> generateAndShowQRCode(evenement));

                        Button addToCalendarButton = new Button("Ajouter au calendrier");
                        addToCalendarButton.setOnAction(event -> addToCalendar(evenement));

                        // Create a horizontal box to hold the buttons
                        HBox buttonBox = new HBox(qrButton, addToCalendarButton);
                        buttonBox.setSpacing(10);

                        setGraphic(buttonBox);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch events from the database.");
        }
    }

    private void addToCalendar(Evenement evenement) {
        try {
            String userHome = "C:\\Users\\ozzy\\Desktop";
            String filePath = userHome + "/event.ics";
            IcsGenerator.generateIcsFile(evenement, filePath);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Event added to calendar successfully!");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add event to calendar.");
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
            byte[] qrCodeImage = QRCodeGenerator.generateQRCode("http://127.0.0.1:8000/eventdetails", evenement.getId());

            InputStream inputStream = new ByteArrayInputStream(qrCodeImage);
            Image qrCode = new Image(inputStream);

            Dialog<Image> dialog = new Dialog<>();
            dialog.setTitle("QR Code");
            dialog.setHeaderText("QR Code for " + evenement.getNom());
            dialog.setResizable(true);

            dialog.getDialogPane().setContent(new ImageView(qrCode));

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(closeButton);

            dialog.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate QR code.");
        }
    }

    public void filterEvenements(KeyEvent keyEvent) {
        String searchText = rechercherid.getText().toLowerCase();
        ObservableList<Evenement> filteredList = FXCollections.observableArrayList();

        if (searchText.isEmpty()) {
            afficherEvenement(); //
            return;
        }

        for (Evenement event : evenementListView.getItems()) {
            if (event.getNom().toLowerCase().contains(searchText)) {
                filteredList.add(event);
            }
        }

        evenementListView.setItems(filteredList);
    }
}

