package controllers.reclamation;

import modeles.reclamation.*;

import service.reclamation.*;
import service.reclamation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ReponsebackController implements Initializable {

    @FXML
    private ComboBox<Reclamation> recid;

    @FXML
    private ListView<ReponseDto> reponse;
    int selectedId;
    int idResponse;
    @FXML
    private TextField text;

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

    void load(){
        reponse.getItems().clear();
        ReponseService rs = new ReponseService();
        rs.afficherJointure().forEach(reclamation -> {
            reponse.getItems().add(reclamation);
        });

        reponse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Populate the TextField with the selected ReponseDto's data
                text.setText(newValue.getTextrespons()); 
                idResponse = newValue.getId();
             
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReclamationService rs = new ReclamationService();
        recid.getItems().addAll(rs.afficher());

        // Add event listener to ComboBox selection
        recid.setOnAction(e -> {
            Reclamation selectedReclamation = recid.getSelectionModel().getSelectedItem();
            if (selectedReclamation != null) {
                 selectedId = selectedReclamation.getId();
                System.out.println("Selected Reclamation ID: " + selectedId);
                // You can do whatever you want with the selected ID here
            }
        });
        load();
    }
}
