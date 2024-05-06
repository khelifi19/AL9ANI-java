package controllers.back.post;

import com.example.al9ani.test.Main;


import controllers.back.MainWindowController;
import utils.etatAct.*;

import service.etaAct.PostService;
import modeles.etaAct.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField titreTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public TextField localisationTF;
    @FXML
    public DatePicker dateDP;
    @FXML
    public ImageView imageIV;


    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Post currentPost;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentPost = ShowAllController.currentPost;

        if (currentPost != null) {
            topText.setText("Modifier post");
            btnAjout.setText("Modifier");

            try {
                titreTF.setText(currentPost.getTitre());
                descriptionTF.setText(currentPost.getDescription());
                localisationTF.setText(currentPost.getLocalisation());
                dateDP.setValue(currentPost.getDate());
                selectedImagePath = FileSystems.getDefault().getPath(currentPost.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }


            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter post");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {
            createImageFile();
            String imagePath = selectedImagePath.toString();

            Post post = new Post();
            post.setTitre(titreTF.getText());
            post.setDescription(descriptionTF.getText());
            post.setLocalisation(localisationTF.getText());
            post.setDate(dateDP.getValue());
            post.setImage(imagePath);


            if (currentPost == null) {
                if (PostService.getInstance().add(post)) {
                    AlertUtils.makeSuccessNotification("Post ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_POST);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                post.setId(currentPost.getId());
                if (PostService.getInstance().edit(post)) {
                    AlertUtils.makeSuccessNotification("Post modifié avec succés");
                    ShowAllController.currentPost = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_POST);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

            if (selectedImagePath != null) {
                createImageFile();
            }
        }
    }

    @FXML
    public void chooseImage(ActionEvent ignored) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.mainStage);
        if (file != null) {
            selectedImagePath = Paths.get(file.getPath());
            imageIV.setImage(new Image(file.toURI().toString()));
        }
    }

    public void createImageFile() {
        try {
            Path newPath = FileSystems.getDefault().getPath("src/main/resources/com/example/al9ani/images/uploads/" + selectedImagePath.getFileName());
            Files.copy(selectedImagePath, newPath, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean controleDeSaisie() {


        if (titreTF.getText().isEmpty()) {
            AlertUtils.makeInformation("titre ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(titreTF.getText())) {
            AlertUtils.makeInformation("titre contient des mots interdits");
            return false;
        }

        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(descriptionTF.getText())) {
            AlertUtils.makeInformation("description contient des mots interdits");
            return false;
        }

        if (localisationTF.getText().isEmpty()) {
            AlertUtils.makeInformation("localisation ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(localisationTF.getText())) {
            AlertUtils.makeInformation("localisation contient des mots interdits");
            return false;
        }

        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }


        return true;
    }
}