package controllers.back.post;


import utils.etatAct.*;
import service.etaAct.*;
import controllers.back.MainWindowController;
import modeles.etaAct.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Post currentPost;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Post> listPost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listPost = PostService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listPost);

        if (!listPost.isEmpty()) {
            for (Post post : listPost) {

                mainVBox.getChildren().add(makePostModel(post));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makePostModel(
            Post post
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_POST)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + post.getTitre());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + post.getDescription());
            ((Text) innerContainer.lookup("#localisationText")).setText("Localisation : " + post.getLocalisation());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + post.getDate());


            Path selectedImagePath = FileSystems.getDefault().getPath(post.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierPost(post));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerPost(post));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterPost(ActionEvent ignored) {
        currentPost = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_POST);
    }

    private void modifierPost(Post post) {
        currentPost = post;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_POST);
    }

    private void supprimerPost(Post post) {
        currentPost = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer post ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (PostService.getInstance().delete(post.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_POST);
                } else {
                    AlertUtils.makeError("Could not delete post");
                }
            }
        }
    }

    public void displayStats(ActionEvent actionEvent) {
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_POST_DISPLAY_STATS);
    }
}
