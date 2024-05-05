package controllers.user;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modeles.user.UserModel;
import service.user.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    private UserModel userModel;
    @FXML
    private ImageView closedEye;
    @FXML
    private TextField newpshow;

    @FXML
    private ImageView openEye;
    @FXML
    private TextField FirstNameS;

    @FXML
    private TextField emailFieldS;

    @FXML
    private Label error_labelS;

    @FXML
    private Label error_labelS1;

    @FXML
    private Hyperlink forgot;
    @FXML
    private TextField picture_input;
    @FXML
    private TextField lastNameFieldS;

    @FXML
    private Button sideButton;
    @FXML
    private AnchorPane sideForm;
    @FXML
    private CheckBox rememberMeCheckbox;


    @FXML
    private Button sideButton1;

    @FXML
    private Button signIn;

    @FXML
    private Button signUpS;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_passwordS;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_usernameS;
    @FXML
    private Hyperlink forgotPass;
    private double x, y;
    private final UserService us = new UserService();


    public SignUpController() {
        userModel = new UserModel();

    }

    @FXML
    public void signup(ActionEvent event) throws IOException {
        // Retrieve user input
        String username = tf_usernameS.getText();
        String password = tf_passwordS.getText();
        String email = emailFieldS.getText();
        String firstName = FirstNameS.getText();
        String lastName = lastNameFieldS.getText();
        String picturePath = picture_input.getText();

        Path path = Paths.get(picturePath);
        String fileName = path.getFileName().toString();

        // Validate email
        if (email.isEmpty() || !email.matches("^[\\w.-]+@esprit\\.tn$")) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email must not be empty & end with @esprit.tn");
            return;
        }

        // Validate password
        if (password == null || password.length() < 6) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 6 characters long");
            return;
        }

        // Check if username exists and is not empty
        boolean check = us.checkIfUsernameExists(username);
        if (username.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Username", "Username must not be empty");
            return;
        }

        // Create a new user model
        UserModel newUser = new UserModel();
        newUser.setFirstName(firstName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setImg(fileName);
        newUser.setRole("USER");

        try {
            if (check) {

                Path destinationPath = Paths.get("src/main/resources/user/img/profilePictures", fileName);
                try {
                    Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            // Attempt to create the new user
            us.create(newUser);
         UserService.setCurrentlyLoggedInUser(newUser);
            // Clear input fields
            tf_passwordS.clear();
            tf_usernameS.clear();
            emailFieldS.clear();
            lastNameFieldS.clear();
            FirstNameS.clear();

            // Navigate to the appropriate page based on the user role
            if (newUser.getRole().equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root));

                primaryStage.initStyle(StageStyle.UNDECORATED);


                root.setOnMousePressed(mouseEvent -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    primaryStage.setX(mouseEvent.getScreenX() - x);
                    primaryStage.setY(mouseEvent.getScreenY() - y);
                });
                primaryStage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                return;

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/profilesetting2.fxml"));
                Parent root = loader.load();
              ProfileSetting profileSetting = loader.getController();
                profileSetting.setUserInformation(username);
                profileSetting.setUserModel(newUser);

                // Set the scene to the primary stage
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("AL9ANI");
                stage.show();

                // Set stage borderless


                // Drag it here
                root.setOnMousePressed(mouseEvent -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    stage.setX(mouseEvent.getScreenX() - x);
                    stage.setY(mouseEvent.getScreenY() - y);
                });
            }}else{
                error_labelS.setVisible(true);
                error_labelS.setText("username already exists");
            }
        } catch (SQLException e) {
            System.err.println(e);

        }
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
    @FXML
    public void login(ActionEvent event) throws IOException, SQLException {

        String username = tf_username.getText();
        String password = tf_password.getText();
        if(tf_username.getText().isEmpty()){
            error_labelS1.setVisible(true);
            error_labelS1.setText("username cannot be empty");

        }
        else {
            if (rememberMeCheckbox.isSelected()) {
                us.rememberUser(username, password);
            } else {
                us.clearRememberedUser();
            }
            boolean isValid = us.checkCredentials(username, password);
            if (username.equals("admin")&&isValid) {
                UserModel newUser= us.readUser(username);
                UserService.setCurrentlyLoggedInUser(newUser);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root));

                primaryStage.initStyle(StageStyle.UNDECORATED);


                root.setOnMousePressed(mouseEvent -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    primaryStage.setX(mouseEvent.getScreenX() - x);
                    primaryStage.setY(mouseEvent.getScreenY() - y);
                });
                primaryStage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                return;
            }
            if (isValid) {
                UserModel newUser= us.readUser(username);
                UserService.setCurrentlyLoggedInUser(newUser);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/profilesetting2.fxml"));
                Parent root = loader.load();
                ProfileSetting profileSetting = loader.getController();

                try {
                    profileSetting.setUserInformation(username);
                } catch (SQLException e) {
                    System.err.println(e);
                }
                // Set the scene to the primary stage
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Home");
                stage.show();
            } else {
                error_labelS1.setVisible(true);
                error_labelS1.setText("Invalid username or password");
            }}
    }
    public void  switchLogin(ActionEvent event)  {
        TranslateTransition slider= new TranslateTransition();
        if(event.getSource()== sideButton){
            slider.setNode(sideForm);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e)->{
                sideButton.setVisible(false);
                sideButton1.setVisible(true);
                tf_username.clear();
                tf_password.clear();
            });
            slider.play();
        } else if (event.getSource()== sideButton1) {
            slider.setNode(sideForm);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e)->{
                sideButton.setVisible(true);
                sideButton1.setVisible(false);
                tf_usernameS.clear();
                tf_passwordS.clear();
                emailFieldS.clear();
                lastNameFieldS.clear();
                FirstNameS.clear();
            });
            slider.play();
        }
    }
    public void setUserModel(UserModel userModel) {
        this.userModel=userModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newpshow.setVisible(false);
        openEye.setVisible(false);
        closedEye.setVisible(true);
        tf_password.setVisible(true);

        openEye.setOnMouseExited(event->{
            String pass=newpshow.getText();
            tf_password.setVisible(true);
            closedEye.setVisible(true);
            tf_password.setText(pass);
            newpshow.setVisible(false);
            openEye.setVisible(false);
        });
        closedEye.setOnMouseClicked(event->{
            String pass=tf_password.getText();
            tf_password.setVisible(false);
            closedEye.setVisible(false);
            newpshow.setText(pass);
            newpshow.setVisible(true);
            openEye.setVisible(true);
        });
        forgotPass.setOnMouseClicked(event->{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/forgotPassword.fxml"));
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

        });
    }
}
