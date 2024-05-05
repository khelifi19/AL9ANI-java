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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modeles.user.UserModel;
import service.user.PasswordHasher;
import service.user.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static service.user.UserService.currentlyLoggedInUser;

public class ProfileSetting implements Initializable {
    private final UserService us = new UserService();

    @FXML
    private Label Confirm;

    @FXML
    private Button DeleteB;
    @FXML
    private ImageView img;

    @FXML
    private TextField picture_input;

    @FXML
    private TextField LastNameU;

    @FXML
    private Button Save;

    @FXML
    private PasswordField confirmPass;
    @FXML
    private MenuItem reset;
    @FXML
    private MenuItem logout;
    @FXML
    private MenuItem update;
    @FXML
    private TextField emailU;

    @FXML
    private TextField firstNameU;

    @FXML
    private Button logoutB;

    @FXML
    private Label newP;
    @FXML
    private ImageView openEye;
    @FXML
    private ImageView imageView;

    @FXML
    private PasswordField newPass;

    @FXML
    private TextField newpshow;
    @FXML
    private Label oldP;

    @FXML
    private Button imgUploader;

    @FXML
    private Circle circle;

    @FXML
    private PasswordField oldPass;

   // @FXML
    //private Button reset;

    @FXML
    private Button resetP;

    @FXML
    private Pane resetPassword;

    @FXML
    private Button showB;

    @FXML
    private Button updateB;

    @FXML
    private Label user;
    @FXML
    private ImageView closedEye;

    @FXML
    private Pane userInfoP;

    private UserModel userM;
    @FXML
    private MenuButton menu;
    public ProfileSetting() {
        this.userM = new UserModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initially, show userInfoP and hide resetPassword
        userInfoP.setVisible(true);
       resetPassword.setVisible(false);

        //newpshow.setVisible(false);
    openEye.setVisible(false);
        closedEye.setVisible(true);
        newPass.setVisible(true);
openEye.setOnMouseExited(event->{
    String pass=newpshow.getText();
    newPass.setVisible(true);
    closedEye.setVisible(true);
    newPass.setText(pass);
    newpshow.setVisible(false);
    openEye.setVisible(false);
});
        closedEye.setOnMouseClicked(event->{
            String pass=newPass.getText();
            newPass.setVisible(false);
            closedEye.setVisible(false);
            newpshow.setText(pass);
            newpshow.setVisible(true);
            openEye.setVisible(true);
        });


      update.setOnAction(event -> {
            userInfoP.setVisible(true);
            resetPassword.setVisible(false);
          imgUploader.setVisible(true);
        });

       reset.setOnAction(event -> {
            // Show resetPassword and hide userInfoP
            userInfoP.setVisible(false);
            resetPassword.setVisible(true);
            imgUploader.setVisible(false);
        });
        menu.setText(UserService.getCurrentlyLoggedInUser().getUsername());

            if (userM != null) {
                firstNameU.setText(userM.getFirstName());
                LastNameU.setText(userM.getLastName());
                emailU.setText(userM.getEmail());
            }


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
    public void upload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                picture_input.setText(selectedFile.getPath());
            } else {
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            System.out.println("No file selected");
        }
    }


    public void setImg(String path,int width,int height) {
        URL imageUrl = getClass().getResource("/user/img/profilePictures/" + path);
        System.out.println(imageUrl);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            img.setImage(image);
            img.setFitWidth(width);
            img.setFitHeight(height);
        } else {
            System.out.println("Image not found");
        }

    }

    public void setUserInformation(String username) throws SQLException {
      //  user.setText("Welcome " + username);
        menu.setText(username);
     this.userM= us.readUser(username);
        if (userM != null) {
            firstNameU.setText(userM.getFirstName());
            LastNameU.setText(userM.getLastName());
            emailU.setText(userM.getEmail());

            picture_input.setText(userM.getImg());
            System.out.println(picture_input.getText());
            URL imageUrl = getClass().getResource("/user/img/profilePictures/" + picture_input.getText());
            System.out.println(imageUrl);
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                circle.setFill(new ImagePattern(image));
                img.setImage(image);
                img.setFitWidth(200);
                img.setFitHeight(200);
            } else {
                System.out.println("Image not found");
            }
        }
    }
    /*
    public void upload_pfp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                picture_input.setText(selectedFile.getPath());
            } else {
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            System.out.println("No file selected");
        }
    }*/
    @FXML
    private void deleteUser(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        // Styling the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/user/alertStyle.css").toExternalForm());
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/signup.fxml"));
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
        dialogPane.getStylesheets().add(getClass().getResource("/user/alertStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                String picturePath = picture_input.getText();
                Path path = Paths.get(picturePath);

                if (Files.exists(path)) { // Check if the file exists
                    String fileName = path.getFileName().toString();
                    userM.setImg(fileName);
                    userM.setFirstName(firstNameU.getText());
                    userM.setLastName(LastNameU.getText());
                    userM.setEmail(emailU.getText());
                    us.update(userM);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Your information has been updated successfully!");
                    alert.showAndWait();
                    setUserInformation(UserService.getCurrentlyLoggedInUser().getUsername());

                    Path destinationPath = Paths.get("src/main/resources/user/img/profilePictures", fileName);
                    try {
                        Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // File not found, show an error message
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The selected file does not exist!");
                    alert.showAndWait();
                }
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
        }
    }

    @FXML
    void reset(ActionEvent event){
        // Retrieve the password values
        String oldPassword = oldPass.getText();
        String newPassword = newPass.getText();
        String confirmPassword = confirmPass.getText();

        // Check if any of the fields are empty
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        // Check if the old password is correct
        if(!PasswordHasher.checkPassword(oldPassword, currentlyLoggedInUser.getPassword())) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The old password is incorrect.");
            return;
        }

        // Check if the new password is at least 3 characters long
        if (newPassword.length() < 3) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The new password must be at least 3 characters long.");
            return;
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The new passwords do not match.");
            return;
        }

        // Update the password
        try {
            currentlyLoggedInUser.setPassword(newPassword);
            // Assuming you have a method in UserService to update the user in the database
            us.resetPassword(newPassword);
            us.showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");

            oldPass.clear();
            newPass.clear();
            confirmPass.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            us.showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating password: " + e.getMessage());
        }
    }

    public void setUserModel(UserModel userModel) {
        this.userM=userModel;
    }
}
