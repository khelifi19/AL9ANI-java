package controllers.evenement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modeles.evenement.Evenement;
import service.serviceEvenement.ServiceEvenement;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EvenementControllerFront {

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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void ajouterEvenement() {
        try {
            ServiceEvenement serviceEvenement = new ServiceEvenement();

            List<Evenement> events = serviceEvenement.selectAll();

            int maxId = events.stream().mapToInt(Evenement::getId).max().orElse(0);

            int id = maxId + 1;

            String nom = tfNom.getText();
            String type = tfType.getText();
            int participants = Integer.parseInt(tfParticipants.getText());
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalDate dateFin = dpDateFin.getValue();

            Evenement evenement = new Evenement(id, nom, type, participants, dateDebut, dateFin);

            serviceEvenement.insertOne(evenement);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Événement ajouté avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de l'événement à la base de données.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un nombre valide pour le nombre de participants.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
        }
    }

    public void afficherEvenements(javafx.event.ActionEvent event) { try {
        // Load the FXML file for the Evenements view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EvenementList.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
    @FXML
    void ajouterPass() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PassFront.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) tfNom.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la vue Pass.");
        }
    }
}
