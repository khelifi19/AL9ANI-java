package controllers.evenement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modeles.evenement.*;
import service.serviceEvenement.*;



import java.sql.SQLException;
import java.util.List;

public class PassTable{
    @FXML
    private ListView<Pass> passListView;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private TextField tfType;

    @FXML
    private TextField tfPrixPass;

    private ObservableList<Pass> passObservableList;
    private ServicePass servicePass;


    @FXML
    void initialize() {
        afficherPass();
        afficherPass();
        passListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Pass item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
    }
    private void afficherPass() {
        try {
            ServicePass servicePass = new ServicePass();
            List<Pass> passes = servicePass.selectAll();

            // Convert the list to an ObservableList
            ObservableList<Pass> passesObservableList = FXCollections.observableArrayList(passes);

            // Set the items in the ListView
            passListView.setItems(passesObservableList);

            // Set a custom cell factory to display the pass and event name in each cell
            passListView.setCellFactory(passListView -> new ListCell<Pass>() {
                @Override
                protected void updateItem(Pass pass, boolean empty) {
                    super.updateItem(pass, empty);
                    if (empty || pass == null) {
                        setText(null);
                    } else {
                        // Display pass details
                        StringBuilder passDetails = new StringBuilder();
                        passDetails.append("Type: ").append(pass.getType()).append("\n");

                        passDetails.append("Prix: ").append(pass.getPrixPass()).append("\n");

                        // Display event name
                        if (pass.getEvenement() != null) {
                            passDetails.append("Événement: ").append(pass.getEvenement().getNom());
                        }

                        setText(passDetails.toString());
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des passes depuis la base de données.");
        }
    }


    @FXML
    void supprimerPass(ActionEvent event) {
        // Get the selected pass from the TableView
        Pass selectedPass = passListView.getSelectionModel().getSelectedItem();

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
    void modifierPass(ActionEvent event) {
        try {
            // Retrieve the selected pass from the TableView
            Pass selectedPass = passListView.getSelectionModel().getSelectedItem();
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
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
