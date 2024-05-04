package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import models.Pass;
import services.ServiceEvenement;
import services.ServicePass;

import javax.security.auth.callback.Callback;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PassControllerBack {
    @FXML
    private TableView<Pass> passTableView;

    @FXML
    private TableColumn<Pass, Integer> colPrixPass;
    @FXML
    private TableColumn<Pass, String> colEvenement;

    @FXML
    private TableColumn<Pass, String> colType;

    @FXML
    private TextField tfType;

    @FXML
    private TextField tfPrixPass;
    @FXML
    private ChoiceBox<Evenement> eventChoiceBox;
    @FXML
    private TextField rechercherid;





    @FXML
    void ajouterPass(ActionEvent event) {
        try {
            ServicePass servicePass = new ServicePass();

            // Retrieve selected event from ChoiceBox
            Evenement selectedEvent = eventChoiceBox.getValue();
            if (selectedEvent == null) {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement.");
                return;
            }

            // Retrieve all passes to find the maximum ID
            List<Pass> passes = servicePass.selectAll();

            int nextId = 0;
            for (Pass p : passes) {
                if (p.getId() > nextId) {
                    nextId = p.getId();
                }
            }

            // Increment the maximum ID by 1 to get the new ID
            int id = nextId + 1;

            // Retrieve pass details from text fields
            String type = tfType.getText();
            int prixPass = Integer.parseInt(tfPrixPass.getText());

            // Create a new Pass object
            Pass pass = new Pass(id, prixPass, type, selectedEvent); // Pass the selected event

            // Insert the new pass into the database
            servicePass.insertOne(pass);

            // Refresh the TableView to display the new pass
            afficherPass();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Pass ajouté avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout du pass à la base de données.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un prix valide pour le pass.");
        } catch (Exception e) {
            // Show generic error message for other exceptions
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
        }
    }


    private void afficherPass() {
        try {
            ServicePass servicePass = new ServicePass();
            List<Pass> passes = servicePass.selectAll();

            passTableView.getItems().clear();

            for (Pass pass : passes) {
                passTableView.getItems().add(pass);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des passes depuis la base de données.");
        }
    }
    @FXML
    void modifierPass(ActionEvent event) {
        try {
            // Retrieve the selected pass from the TableView
            Pass selectedPass = passTableView.getSelectionModel().getSelectedItem();
            if (selectedPass == null) {
                showAlert(Alert.AlertType.WARNING, "Aucun pass sélectionné", "Veuillez sélectionner un pass à modifier.");
                return;
            }

            // Retrieve the updated details from the text fields
            String type = tfType.getText();
            int prixPass = Integer.parseInt(tfPrixPass.getText());

            // Create an updated Pass object with the new details, retaining the existing Evenement
            Pass updatedPass = new Pass(selectedPass.getId(), prixPass, type, selectedPass.getEvenement());

            // Update the pass in the database
            ServicePass servicePass = new ServicePass();
            servicePass.updateOne(updatedPass);

            // Refresh the TableView to reflect the changes
            afficherPass();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pass modifié avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la modification du pass dans la base de données.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un prix valide pour le pass.");
        } catch (Exception e) {
            // Show generic error message for other exceptions
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
        }
    }
    @FXML
    void supprimerPass(ActionEvent event) {
        // Get the selected pass from the TableView
        Pass selectedPass = passTableView.getSelectionModel().getSelectedItem();

        if (selectedPass != null) {
            try {
                // Delete the selected pass from the database
                ServicePass servicePass = new ServicePass();
                servicePass.deleteOne(selectedPass);

                // Refresh the TableView to reflect the changes
                afficherPass();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Pass supprimé avec succès !");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la suppression du pass dans la base de données.");
            }
        }

    }


    @FXML


    private void populateEventChoiceBox() {
        try {
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            List<Evenement> events = serviceEvenement.selectAll();


            eventChoiceBox.getItems().addAll(events);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des événements depuis la base de données.");
        }
    }
    @FXML
    void initialize() {
        colPrixPass.setCellValueFactory(new PropertyValueFactory<>("prixPass"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colEvenement.setCellValueFactory(cellData -> {
            Pass pass = cellData.getValue();
            return new SimpleStringProperty(pass.getEvenement().getNom());
        });

        afficherPass();

        populateEventChoiceBox();

    }
    @FXML
    void ajouterEvenement() {
        try {
            // Load the FXML file for the EvenementBack view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EvenementBack.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage from the current scene
            Stage stage = (Stage) tfType.getScene().getWindow();

            // Set the new scene to the stage, and show it
            stage.setScene(scene);
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

    public void filterPass(javafx.scene.input.KeyEvent keyEvent) {
        String keyword = rechercherid.getText().toLowerCase();

        if (keyword.isEmpty()) {
            afficherPass();
            return;
        }

        FilteredList<Pass> filteredData = new FilteredList<>(passTableView.getItems(), p -> true);

        // Set the predicate to filter the data based on the keyword
        filteredData.setPredicate(pass -> {
            // Convert pass details to lowercase for case-insensitive comparison
            String lowerCaseKeyword = keyword.toLowerCase();

            // Check if any pass attribute contains the keyword
            return pass.getType().toLowerCase().contains(lowerCaseKeyword)
                    || pass.getEvenement().getNom().toLowerCase().contains(lowerCaseKeyword);
        });

        // Update the pass table view with the filtered data
        passTableView.setItems(filteredData);
    }
}
