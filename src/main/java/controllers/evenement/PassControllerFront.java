package controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Evenement;
import models.Pass;
import services.ServiceEvenement;
import services.ServicePass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PassControllerFront {
    @FXML
    private TextField tfType;

    @FXML
    private TextField tfPrixPass;

    @FXML
    private ChoiceBox<Evenement> eventChoiceBox;
    @FXML
    private Button afficherButton;

    @FXML
    void initialize() {
        populateEventChoiceBox();
    }

    @FXML
    void ajouterPass(ActionEvent event) {
        try {
            ServicePass servicePass = new ServicePass();

            Evenement selectedEvent = eventChoiceBox.getValue();
            if (selectedEvent == null) {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement.");
                return;
            }

            List<Pass> passes = servicePass.selectAll();
            int nextId = 0;
            for (Pass p : passes) {
                if (p.getId() > nextId) {
                    nextId = p.getId();
                }
            }

            int id = nextId + 1;
            String type = tfType.getText();
            int prixPass = Integer.parseInt(tfPrixPass.getText());

            Pass pass = new Pass(id, prixPass, type, selectedEvent);
            servicePass.insertOne(pass);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Pass ajouté avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout du pass à la base de données.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un prix valide pour le pass.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
        }
    }

    @FXML
    void afficherPassList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PassListFront.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateEventChoiceBox() {
        try {
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            List<Evenement> events = serviceEvenement.selectAll();
            eventChoiceBox.setItems(FXCollections.observableArrayList(events));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des événements depuis la base de données.");
        }
    }
    @FXML
    void ajouterEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EvenementFront.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la vue EvenementBack.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
