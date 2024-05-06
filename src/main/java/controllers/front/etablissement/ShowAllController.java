package controllers.front.etablissement;

import service.etaAct.*;
import utils.etatAct.*;
import modeles.etaAct.*;
import controllers.front.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    public VBox mainVBox;
    @FXML
    public TextField searchField;


    List<Etablissement> listEtablissement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listEtablissement = EtablissementService.getInstance().getAll();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listEtablissement);

        if (!listEtablissement.isEmpty()) {
            for (Etablissement etablissement : listEtablissement) {
                if (searchText.isEmpty() || etablissement.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeEtablissementModel(etablissement));
                }
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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_ETABLISSEMENT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + etablissement.getNom());
            ((Text) innerContainer.lookup("#categorieText")).setText("Categorie : " + etablissement.getCategorie());
            ((Text) innerContainer.lookup("#adresseText")).setText("Adresse : " + etablissement.getAdresse());
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + etablissement.getEmail());
            ((Text) innerContainer.lookup("#telephoneText")).setText("Telephone : " + etablissement.getTelephone());

            ((Text) innerContainer.lookup("#userText")).setText("User : " + etablissement.getUser().toString());


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }


    public void searchEtablissement(KeyEvent keyEvent) {
        displayData(searchField.getText());
    }
}
