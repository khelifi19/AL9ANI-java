package controllers.reclamation;

import controllers.uber.AdminController;
import controllers.user.dashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modeles.reclamation.*;

import service.reclamation.*;
import service.reclamation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
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
    private Button btnOverview;

    @FXML
    private Button etabB;

    @FXML
    private Button eventB;

    @FXML
    private Label fruitNameLable11;

    @FXML
    private Label fruitNameLable111;

    @FXML
    private Button recB;

    @FXML
    private Button reponseB;

    @FXML
    private TextField text;

    @FXML
    private Button uberB;

    @FXML
    private Button userb;
    private void redirectToAcceuil() {
        System.out.println("Redirection vers l accueil...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/reclamation/reclamation.fxml"));
            Parent root = loader.load();
                ReclamationController homeUber = loader.getController();
            if (homeUber == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if ( reponseB != null) {
                stage = (Stage) reponseB.getScene().getWindow();
            }
            else {
                System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                return;
            }

            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }
    @FXML
    private void redirectTo(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        System.out.println(buttonId);
        switch (buttonId) {
            case "userb" :

                FXMLLoader loa = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent ro = null;
                try {
                    ro = loa.load();
                    dashboardController dashboardController = loa.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage ps = new Stage();
                ps.setScene(new Scene(ro));

                ps.initStyle(StageStyle.UNDECORATED);



                ps.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;

            case "uberB":
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
                    Parent root = loader.load();
                    AdminController homeUber = loader.getController();
                    if (homeUber == null) {
                        System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                        return;
                    }

                    Scene scene = new Scene(root);
                    Stage stage;
                    if (uberB != null) {
                        stage = (Stage) uberB.getScene().getWindow();
                    }
                    else {
                        System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                        return;
                    }

                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Redirection réussie !");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de la redirection: " + e.getMessage());
                }

                break;
            case "etabB":

                break;
            case "eventB":
                // Redirection vers la page des événements
                // Exemple : goToEventPage();
                break;
            case "recB":
                // Redirection vers la page de recommandations
                // Exemple : goToRecommendationPage();
                break;
            case "btnOverview":
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                    dashboardController dashboardController = loader.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root));

                primaryStage.initStyle(StageStyle.UNDECORATED);



                primaryStage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;
            default:
                // Action par défaut si aucun cas ne correspond
                break;
        }
    }
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
        reponseB.setOnAction(event -> {redirectToAcceuil();});
        load();
    }
}
