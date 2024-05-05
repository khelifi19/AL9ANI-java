package controllers.evenement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import modeles.evenement.Evenement;
import service.serviceEvenement.ServiceEvenement;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EvenementListController {

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

            // Convert the list to an ObservableList
            ObservableList<Evenement> evenementsObservableList = FXCollections.observableArrayList(evenements);

            // Set the items in the ListView
            evenementListView.setItems(evenementsObservableList);

            // Set a custom cell factory to display the event details in each cell
            evenementListView.setCellFactory(eventListView -> new ListCell<Evenement>() {
                @Override
                protected void updateItem(Evenement evenement, boolean empty) {
                    super.updateItem(evenement, empty);
                    if (empty || evenement == null) {
                        setText(null);
                    } else {
                        // Display all event details
                        setText("Nom: " + evenement.getNom() + "\n" +
                                "Type: " + evenement.getType() + "\n" +
                                "Participants: " + evenement.getParticipants() + "\n" +
                                "Date début: " + evenement.getDateDebut() + "\n" +
                                "Date fin: " + evenement.getDateFin());
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





    public void modifierEvenement(javafx.event.ActionEvent actionEvent) {try {
        // Retrieve the selected event from the ListView
        Evenement selectedEvent = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        // Retrieve the updated details from the input fields
        String nom = tfNom.getText();
        String type = tfType.getText();
        int participants = Integer.parseInt(tfParticipants.getText());
        LocalDate dateDebut = dpDateDebut.getValue();
        LocalDate dateFin = dpDateFin.getValue();

        // Create an updated Evenement object with the new details
        Evenement updatedEvent = new Evenement(selectedEvent.getId(), nom, type, participants, dateDebut, dateFin);

        // Update the event in the database
        ServiceEvenement se = new ServiceEvenement();
        se.updateOne(updatedEvent);

        // Refresh the ListView to reflect the changes
        afficherEvenement();

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié avec succès !");
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la modification de l'événement dans la base de données.");
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un numéro valide !");
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
    }
    }

    public void supprimerEvenement(javafx.event.ActionEvent actionEvent) {

        try {
            Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
            if (selectedEvenement == null) {
                showAlert(Alert.AlertType.WARNING, "Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
                return;
            }

            ServiceEvenement serviceEvenement = new ServiceEvenement();
            serviceEvenement.deleteOne(selectedEvenement);
            afficherEvenement();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la suppression de l'événement.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite. Veuillez réessayer.");
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




