package controllers.back.etablissement;



import modeles.etaAct.*;
import modeles.user.UserModel;
import service.etaAct.*;
import utils.etatAct.*;
import controllers.back.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public TextField nomTF;
    @FXML
    public TextField categorieTF;
    @FXML
    public TextField adresseTF;
    @FXML
    public TextField emailTF;
    @FXML
    public TextField telephoneTF;

    @FXML
    public ComboBox<UserModel> userCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Etablissement currentEtablissement;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (UserModel user : EtablissementService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        currentEtablissement = ShowAllController.currentEtablissement;

        if (currentEtablissement != null) {
            topText.setText("Modifier etablissement");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentEtablissement.getNom());
                categorieTF.setText(currentEtablissement.getCategorie());
                adresseTF.setText(currentEtablissement.getAdresse());
                emailTF.setText(currentEtablissement.getEmail());
                telephoneTF.setText(String.valueOf(currentEtablissement.getTelephone()));

                userCB.setValue(currentEtablissement.getUser());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter etablissement");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Etablissement etablissement = new Etablissement();
            etablissement.setNom(nomTF.getText());
            etablissement.setCategorie(categorieTF.getText());
            etablissement.setAdresse(adresseTF.getText());
            etablissement.setEmail(emailTF.getText());
            etablissement.setTelephone(Integer.parseInt(telephoneTF.getText()));

            etablissement.setUser(userCB.getValue());

            if (currentEtablissement == null) {
                if (EtablissementService.getInstance().add(etablissement)) {
                    AlertUtils.makeSuccessNotification("Etablissement ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ETABLISSEMENT);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                etablissement.setId(currentEtablissement.getId());
                if (EtablissementService.getInstance().edit(etablissement)) {
                    AlertUtils.makeSuccessNotification("Etablissement modifié avec succés");
                    ShowAllController.currentEtablissement = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ETABLISSEMENT);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (nomTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nom ne doit pas etre vide");
            return false;
        }


        if (categorieTF.getText().isEmpty()) {
            AlertUtils.makeInformation("categorie ne doit pas etre vide");
            return false;
        }


        if (adresseTF.getText().isEmpty()) {
            AlertUtils.makeInformation("adresse ne doit pas etre vide");
            return false;
        }


        if (emailTF.getText().isEmpty()) {
            AlertUtils.makeInformation("email ne doit pas etre vide");
            return false;
        }
        if (!Pattern.compile("^(.+)@(.+)$").matcher(emailTF.getText()).matches()) {
            AlertUtils.makeInformation("Email invalide");
            return false;
        }


        if (telephoneTF.getText().isEmpty()) {
            AlertUtils.makeInformation("telephone ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(telephoneTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("telephone doit etre un nombre");
            return false;
        }

        if (telephoneTF.getText().length() != 8){
            AlertUtils.makeInformation("telephone doit avoir 8 chiffres");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}