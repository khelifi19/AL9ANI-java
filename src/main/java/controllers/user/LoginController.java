package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.UserModel;
import service.user.UserService;

import java.io.IOException;

public class LoginController {
    private UserModel userModel;
    private final UserService us = new UserService();


    @FXML
    private TextField tf_username;
    @FXML private PasswordField tf_password;
    @FXML private Label error_label;

    public LoginController() {
        userModel = new UserModel();
    }

    @FXML
    public void login(ActionEvent event)throws IOException{

        String username = tf_username.getText();
        String password = tf_password.getText();
if(tf_username.getText().isEmpty()){
    error_label.setText("username cannot be empty");

}
else {

        boolean isValid = us.checkCredentials(username, password);

        if (isValid) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/HomePage.fxml"));
            Parent root = loader.load();
            HomeController homeController = loader.getController();
            homeController.setUserInformation(username);
            // Set the scene to the primary stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } else {
            error_label.setText("Invalid username or password");
        }}
    }
   public void  switchSignUp(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/signup.fxml"));
       Parent root = null;
       try {
           root = loader.load();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

       // Set the scene to the primary stage
       Scene scene = new Scene(root);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setScene(scene);
       stage.setTitle("SignUp");
       stage.show();
   }

    public void setUserModel(UserModel userModel) {
        this.userModel=userModel;
    }
}
