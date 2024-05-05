package controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import modeles.user.UserModel;
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Navbar implements Initializable {
    @FXML
    private Circle circle;

    @FXML
    private MenuItem logout;

    @FXML
    private MenuButton menu;

    @FXML
    private MenuItem reset;

    @FXML
    private MenuItem update;

    @FXML
    private TextField search;
    private UserService us=new UserService();
    private UserModel userM= new UserModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.setText(UserService.getCurrentlyLoggedInUser().getUsername());
        setImg(UserService.currentlyLoggedInUser.getImg());
        update.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/profileSetting2.fxml"));
            Parent root= null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProfileSetting profileSetting = loader.getController();
                 try {
                        profileSetting.setUserInformation(UserService.getCurrentlyLoggedInUser().getUsername());
                    } catch (SQLException e) {
                        System.err.println(e);
                    }
            // Set the scene to the primary stage
            Scene scene = new Scene(root);


            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();

        });

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    us.clearRememberedUser();
                    // Load the login.fxml file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/signup.fxml"));
                    Parent root = loader.load();

                    // Create a new scene with the login page content
                    Scene scene = new Scene(root);

                    // Get the stage (window) from the button's action event
                    Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();

                    // Set the scene to the stage and show it
                    stage.setScene(scene);
                    stage.setTitle("Login");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void setUserInformation(String username) throws SQLException {
        this.userM= us.readUser(username);
        menu.setText(username);
        setImg(userM.getImg());



    }
    public void setImg(String path) {
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
