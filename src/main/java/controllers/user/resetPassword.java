package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.user.UserModel;
import service.user.OtpManager;
import service.user.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class resetPassword {
    @FXML
    private Button loginP;

    @FXML
    private Button tryAgain;
    @FXML
    private PasswordField OnePassword;

    @FXML
    private ImageView back;

    @FXML
    private Label error_labelS;
    @FXML
    private Button validate;
    @FXML
    private TextField Email;

    @FXML
    private ImageView arrow;

    @FXML
    private Button sending;
    private final UserService userS = new UserService();
    private  UserModel userM = new UserModel();
    @FXML
    private void sendOTP(ActionEvent event) {
        String email = Email.getText();
        if (userS.checkIfEmailExists(email)){
            // Generate a one-time password
            String otp = OtpManager.generateOTP(email);
            // Send the one-time password to the user's email (You need to implement this)


        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/error.fxml"));
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
            stage.setTitle("error");
            stage.show();
        }

    }
    public void  switchForgot(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/forgotPassword.fxml"));
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


    // Method to handle resetting the password
    @FXML
    private void update(ActionEvent event) {
        String email = Email.getText();
        String otp = OnePassword.getText();
        // Verify the one-time password
        if (OtpManager.verifyOTP(email, otp)!=null) {
            try {
                userM=userS.readUserE(email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/reset.fxml"));
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
            stage.setTitle("update");
            stage.show();


        } else {
            error_labelS.setVisible(true);
            error_labelS.setText("username already exists");
        }
    }
    @FXML
    private void done(ActionEvent event) {
            try {
        userS.update(userM);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/profileSetting.fxml"));
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
                stage.setTitle("profile");
                stage.show();

    }catch (SQLException e){
        throw new RuntimeException(e);
    }
}}
