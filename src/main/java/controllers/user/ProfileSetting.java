package controllers.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.user.UserModel;
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileSetting implements Initializable {
    private final UserService us = new UserService();
    @FXML
    private Button DeleteB;

    @FXML
    private TextField LastNameU;

    @FXML
    private Button Save;

    @FXML
    private TextField emailU;

    @FXML
    private TextField firstNameU;

    @FXML
    private Button logoutB;

    @FXML
    private Button showB;

    @FXML
    private Button updateB;

    @FXML
    private Label user;
    private UserModel userM;

    public ProfileSetting() {
        this.userM = new UserModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



            if (userM != null) {
                firstNameU.setText(userM.getFirstName());
                System.out.println(userM.getEmail());
                LastNameU.setText(userM.getLastName());
                emailU.setText(userM.getEmail());
            }


        logoutB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    // Load the login.fxml file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/signup.fxml"));
                    Parent root = loader.load();

                    // Create a new scene with the login page content
                    Scene scene = new Scene(root);

                    // Get the stage (window) from the button's action event
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

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
        user.setText("Welcome " + username);
     this.userM= us.readUser(username);
        if (userM != null) {
            firstNameU.setText(userM.getFirstName());
            System.out.println(userM.getEmail());
            LastNameU.setText(userM.getLastName());
            emailU.setText(userM.getEmail());
        }
    }
    @FXML
    private void deleteUser(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        // Styling the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/view/user/alertStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        // Set custom buttons
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            try {
                us.delete(userM.getUsername());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been deleted successfully!");
                alert.showAndWait();

                // Redirect to signup page after deleting the account
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/signup.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login");
                stage.show();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while deleting your account!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void update(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to update your information?");
        alert.setContentText("Click OK to confirm, or Cancel to return.");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/view/user/alertStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional <ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                userM.setFirstName(firstNameU.getText());
                userM.setLastName(LastNameU.getText());
                userM.setEmail(emailU.getText());
                us.update(userM);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Your information has been updated successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating your information!");
                alert.showAndWait();
            }
        } else if (result.get() == ButtonType.CANCEL) {
            // Reload user's old information and update text fields
            try {

                setUserInformation(userM.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }}

    public void setUserModel(UserModel userModel) {
        this.userM=userModel;
    }
}
