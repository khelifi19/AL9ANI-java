package controllers.back.commentaire;



import controllers.back.MainWindowController;
import modeles.etaAct.*;
import modeles.user.UserModel;
import service.etaAct.*;
import utils.etatAct.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField descriptionTF;
    @FXML
    public DatePicker dateDP;

    @FXML
    public ComboBox<Post> postCB;
    @FXML
    public ComboBox<UserModel> userCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Commentaire currentCommentaire;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Post post : PostService.getInstance().getAll()) {
            postCB.getItems().add(post);
        }

        for (UserModel user : CommentaireService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        currentCommentaire = ShowAllController.currentCommentaire;

        if (currentCommentaire != null) {
            topText.setText("Modifier commentaire");
            btnAjout.setText("Modifier");

            try {
                descriptionTF.setText(currentCommentaire.getDescription());
                dateDP.setValue(currentCommentaire.getDate());

                postCB.setValue(currentCommentaire.getPost());
                userCB.setValue(currentCommentaire.getUser());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter commentaire");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Commentaire commentaire = new Commentaire();
            commentaire.setDescription(descriptionTF.getText());
            commentaire.setDate(dateDP.getValue());

            commentaire.setPost(postCB.getValue());
            commentaire.setUser(userCB.getValue());

            if (currentCommentaire == null) {
                if (CommentaireService.getInstance().add(commentaire)) {
                    AlertUtils.makeSuccessNotification("Commentaire ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMENTAIRE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                commentaire.setId(currentCommentaire.getId());
                if (CommentaireService.getInstance().edit(commentaire)) {
                    AlertUtils.makeSuccessNotification("Commentaire modifié avec succés");
                    ShowAllController.currentCommentaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMENTAIRE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(descriptionTF.getText())) {
            AlertUtils.makeInformation("description contient des mots interdits");
            return false;
        }


        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (postCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un post");
            return false;
        }
        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}