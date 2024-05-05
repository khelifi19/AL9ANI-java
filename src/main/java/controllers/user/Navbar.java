package controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import modeles.user.UserModel;
import service.user.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class Navbar implements Initializable {
    @FXML
    private Circle circle;

    @FXML
    private MenuButton menu;

    @FXML
    private TextField search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.setText(UserService.getCurrentlyLoggedInUser().getUsername());

    }
    public void setImg(String path,int width,int height) {
        URL imageUrl = getClass().getResource("/user/img/profilePictures/" + path);
        System.out.println(imageUrl);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            circle.setFill(new ImagePattern(image));
        } else {
            System.out.println("Image not found");
        }

    }
}
