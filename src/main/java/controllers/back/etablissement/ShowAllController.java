package controllers.back.etablissement;


import controllers.back.MainWindowController;
import modeles.etaAct.*;
import service.etaAct.*;
import utils.etatAct.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Etablissement currentEtablissement;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Etablissement> listEtablissement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listEtablissement = EtablissementService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listEtablissement);

        if (!listEtablissement.isEmpty()) {
            for (Etablissement etablissement : listEtablissement) {

                mainVBox.getChildren().add(makeEtablissementModel(etablissement));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeEtablissementModel(
            Etablissement etablissement
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_ETABLISSEMENT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + etablissement.getNom());
            ((Text) innerContainer.lookup("#categorieText")).setText("Categorie : " + etablissement.getCategorie());
            ((Text) innerContainer.lookup("#adresseText")).setText("Adresse : " + etablissement.getAdresse());
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + etablissement.getEmail());
            ((Text) innerContainer.lookup("#telephoneText")).setText("Telephone : " + etablissement.getTelephone());

            ((Text) innerContainer.lookup("#userText")).setText("User : " + etablissement.getUser().toString());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierEtablissement(etablissement));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerEtablissement(etablissement));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterEtablissement(ActionEvent ignored) {
        currentEtablissement = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ETABLISSEMENT);
    }

    private void modifierEtablissement(Etablissement etablissement) {
        currentEtablissement = etablissement;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ETABLISSEMENT);
    }

    private void supprimerEtablissement(Etablissement etablissement) {
        currentEtablissement = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer etablissement ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (EtablissementService.getInstance().delete(etablissement.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ETABLISSEMENT);
                } else {
                    AlertUtils.makeError("Could not delete etablissement");
                }
            }
        }
    }

    public void showStatistics(ActionEvent actionEvent) {
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_STATISTICS_ETABLISSEMENT);
    }
}
