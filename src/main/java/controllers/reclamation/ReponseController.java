package controllers.reclamation;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.reclamation.*;
import modeles.reclamation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class ReponseController implements Initializable {

    @FXML
    private ComboBox<Reclamation> recid;

    @FXML
    private TextField text;
    @FXML

   private Button AjouterReclamation;
    @FXML
    private TilePane myTilePane;

    int selectedId;
    int idResponse;

    @FXML
    void add(ActionEvent event) {
        ReponseService rps = new ReponseService();

        if (isValidInput()) {
            rps.ajouter(new Reponse(selectedId, text.getText()));

            // Update Reclamation state to "Valide" (1)
            updateReclamationState(selectedId);

            // Reload data after adding
            load();
        }
    }

    private void updateReclamationState(int reclamationId) {
        ReclamationService rs = new ReclamationService();
        Reclamation reclamation = rs.getReclamationById(reclamationId);

        if (reclamation != null) {
            // Update the Reclamation state to "Valide" (1)
            reclamation.setEtat(1);
            rs.modifier(reclamation);
        }
    }


    private boolean isValidInput() {
        if (text.getText().isEmpty() || recid.getValue() == null) {
            // Show an alert or error message indicating missing fields
            showAlert("Error", "Please fill in all the fields.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void delete(ActionEvent event) {
        ReponseService rs = new ReponseService();
        rs.supprimer(new Reponse(idResponse));
        load();
    }

    @FXML
    void update(ActionEvent event) {
        ReponseService rs = new ReponseService();
        rs.modifier(new Reponse(idResponse, selectedId, text.getText()));
        load();
    }

    @FXML
    private TextField searchField;

    void load() {
        ReponseService rs = new ReponseService(); // Assuming you have a service class
        List<Reponse> Reponses = rs.afficher(); // Call the method to retrieve reclamations

        myTilePane.getChildren().clear(); // Clear existing content
        for (Reponse Reponse : Reponses) {
            VBox card = createCard(Reponse); // Assuming createCard creates a VBox card for each publication
            myTilePane.getChildren().add(card);
        }
    }

    private VBox createCard(Reponse reponse) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card-style");

        // Customize your card layout here, e.g., add labels, buttons, etc.
        Label labelObjet = new Label("Text: " + reponse.getText());
        labelObjet.getStyleClass().add("card-title");

        // Set a maximum width for the labels to prevent them from expanding too much
        labelObjet.setMaxWidth(200);

        // Enable wrapping for the labels
        labelObjet.setWrapText(true);

        // Add a separator to visually separate the fields
        Separator separator = new Separator();
        separator.setPrefWidth(200);

        // Add the image
        try {
            Path destinationPath = Paths.get("src/main/resources/reclamation/assets", "images.png");
            FileInputStream inputStream = new FileInputStream(String.valueOf(destinationPath));
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100); // Adjust the width of the image as needed
            imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image

            card.getChildren().addAll(labelObjet, imageView, separator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return card;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReclamationService rs = new ReclamationService();
        AjouterReclamation.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/reclamationBack.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ReclamationBackController reclamationController = loader.getController();

            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) AjouterReclamation.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
        load();
    }
}
