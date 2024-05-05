package controllers.user;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import modeles.user.UserModel;
import service.user.UserService;

import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
    @FXML
    private Button logout;
    @FXML
    private Label user;

    @FXML
    private Navbar navbarController;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // String username = UserService.getCurrentlyLoggedInUser().getUsername();

      /*  try {
            navbarController.setUserInformation(username);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception as needed
        }*/



    }




}
