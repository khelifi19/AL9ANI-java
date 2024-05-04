package controllers;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Evenement;
import services.ServiceEvenement;

public class EvenementControllerBack {

    @FXML
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
    private TableView<Evenement> eventTableView;

    @FXML
    private TableColumn<Evenement, String> colNom;

    @FXML
    private TableColumn<Evenement, String> colType;

    @FXML
    private TableColumn<Evenement, Integer> colParticipants;

    @FXML
    private TableColumn<Evenement, LocalDate> colDateDebut;

    @FXML
    private TableColumn<Evenement, LocalDate> colDateFin;
    @FXML
    private TextField rechercherid;
    private FilteredList<Evenement> filteredEvents;


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void ajouterEvenement(ActionEvent event) {
        try {
            ServiceEvenement serviceEvenement = new ServiceEvenement();

            List<Evenement> events = serviceEvenement.selectAll();

            int nxtId = 0;
            for (Evenement e : events) {
                if (e.getId() > nxtId) {
                    nxtId = e.getId();
                }
            }

            int id = nxtId + 1;

            String nom = tfNom.getText();
            String type = tfType.getText();
            int participants = Integer.parseInt(tfParticipants.getText());
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalDate dateFin = dpDateFin.getValue();

            Evenement evenement = new Evenement(id, nom, type, participants, dateDebut, dateFin);

            serviceEvenement.insertOne(evenement);
            afficherEvenement();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Événement ajouté avec succès !");
            alert.show();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setContentText("Erreur lors de l'ajout de l'événement à la base de données.");
            alert.show();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Veuillez saisir un nombre valide pour le nombre de participants.");
            alert.show();
        } catch (Exception e) {
            // Show generic error message for other exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur s'est produite. Veuillez réessayer.");
            alert.show();
        }
    }
    @FXML
    void modifierEvenement(ActionEvent event) {
        try {
            String nom = tfNom.getText();
            String type = tfType.getText();
            int participants = Integer.parseInt(tfParticipants.getText());
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalDate dateFin = dpDateFin.getValue();

            Evenement selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
            if (selectedEvent == null) {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
                return;
            }

            Evenement updatedEvent = new Evenement(selectedEvent.getId(), nom, type, participants, dateDebut, dateFin);

            ServiceEvenement se = new ServiceEvenement();
            se.updateOne(updatedEvent);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié avec succès !");
            afficherEvenement();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la modification de l'événement dans la base de données.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un numéro valide !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
        }
    }

    @FXML
    void supprimerEvenement(ActionEvent event) {
        Evenement selectedEvent = eventTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            try {
                ServiceEvenement se = new ServiceEvenement();
                se.deleteOne(selectedEvent);

                afficherEvenement();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès !");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la suppression de l'événement.");
            }
        }
    }



    private void afficherEvenement() {
        try {
            ServiceEvenement se = new ServiceEvenement();
            List<Evenement> evenements = se.selectAll();

            ObservableList<Evenement> data = FXCollections.observableArrayList(evenements);
            eventTableView.setItems(data);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des événements depuis la base de données.");
        }
    }
    @FXML
    void ajouterPass(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PassBack.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la vue PassBack.");
        }
    }

    @FXML
    void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colParticipants.setCellValueFactory(new PropertyValueFactory<>("participants"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        afficherEvenement();
    }

    public void filterEvenements(javafx.scene.input.KeyEvent keyEvent) {

        String keyword = rechercherid.getText().toLowerCase();

        if (keyword.isEmpty()) {
            afficherEvenement();
            return;
        }

        FilteredList<Evenement> filteredData = new FilteredList<>(eventTableView.getItems(), e -> true);

        filteredData.setPredicate(evenement -> {
            String lowerCaseKeyword = keyword.toLowerCase();

            return evenement.getNom().toLowerCase().contains(lowerCaseKeyword)
                    || evenement.getType().toLowerCase().contains(lowerCaseKeyword);
        });

        eventTableView.setItems(filteredData);
    }
}
