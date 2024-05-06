package controllers.back.commentaire;



import controllers.back.*;
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

    public static Commentaire currentCommentaire;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Commentaire> listCommentaire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCommentaire = CommentaireService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCommentaire);

        if (!listCommentaire.isEmpty()) {
            for (Commentaire commentaire : listCommentaire) {

                mainVBox.getChildren().add(makeCommentaireModel(commentaire));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCommentaireModel(
            Commentaire commentaire
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_COMMENTAIRE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + commentaire.getDescription());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + commentaire.getDate());

            ((Text) innerContainer.lookup("#postText")).setText("Post : " + commentaire.getPost().toString());
            ((Text) innerContainer.lookup("#userText")).setText("User : " + commentaire.getUser().toString());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCommentaire(commentaire));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCommentaire(commentaire));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCommentaire(ActionEvent ignored) {
        currentCommentaire = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_COMMENTAIRE);
    }

    private void modifierCommentaire(Commentaire commentaire) {
        currentCommentaire = commentaire;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_COMMENTAIRE);
    }

    private void supprimerCommentaire(Commentaire commentaire) {
        currentCommentaire = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer commentaire ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (CommentaireService.getInstance().delete(commentaire.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMENTAIRE);
                } else {
                    AlertUtils.makeError("Could not delete commentaire");
                }
            }
        }
    }


}
