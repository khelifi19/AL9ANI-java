package controller.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.user.UserModel;
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;
    @FXML
    private TextField searchInput;
    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    @FXML
    private Label totalU;

    private final UserService us = new UserService();
    private UserModel userM;
    List<UserModel> users;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSignout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    us.clearRememberedUser();
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
        try {
            users = us.read();
            for (int i = 0; i < users.size(); i++) {
                final int j = i;
                HBox node;
                try {
                    totalU.setText(String.valueOf(users.size()));
                    node = FXMLLoader.load(getClass().getResource("/view/user/adminD/Item.fxml"));
                    HBox.setHgrow(node, Priority.ALWAYS);
                    setUserInformation(node, users.get(i).getUsername(), users.get(i).getEmail(), users.get(i).getFirstName(), users.get(i).getLastName());
                    pnItems.getChildren().add(node);

                    // Give the items some effect
                    node.setOnMouseEntered(event -> {
                        node.setStyle("-fx-background-color : #EBE8F9");
                    });
                    node.setOnMouseExited(event -> {
                        node.setStyle("-fx-background-color : white");
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : white");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
    }

    // Set User Information
    private void setUserInformation(HBox node, String username, String email, String firstName, String lastName) {
        Label usenameL = (Label) node.lookup("#usename");
        Label f_name = (Label) node.lookup("#f_name");
        Label l_name = (Label) node.lookup("#l_name");
        Label userEmail = (Label) node.lookup("#email");
        Button deleteU= (Button) node.lookup("#delete");
        deleteU.setUserData(username);
        deleteU.setOnAction(event -> deleteUser(username));
        usenameL.setText(username);
        userEmail.setText(email);
        f_name.setText(firstName);
        l_name.setText(lastName);
        userEmail.setWrapText(true);
        f_name.setWrapText(true);
        l_name.setWrapText(true);
        usenameL.setWrapText(true);
    }
    @FXML
    private void deleteUser(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete this user?");
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
                us.delete(username);
    reloadData();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("this account has been deleted successfully!");
                alert.showAndWait();



            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while deleting this account!");
                alert.showAndWait();
            }
        }
    }
    public void reloadData(){
        pnItems.getChildren().clear();
        try {
            users = us.read();
            for (int i = 0; i < users.size(); i++) {
                final int j = i;
                HBox node;
                try {
                    totalU.setText(String.valueOf(users.size()));
                    node = FXMLLoader.load(getClass().getResource("/view/user/adminD/Item.fxml"));
                    setUserInformation(node, users.get(i).getUsername(), users.get(i).getEmail(), users.get(i).getFirstName(), users.get(i).getLastName());
                    pnItems.getChildren().add(node);

                    // Give the items some effect
                    node.setOnMouseEntered(event -> {
                        node.setStyle("-fx-background-color : #EBE8F9");
                    });
                    node.setOnMouseExited(event -> {
                        node.setStyle("-fx-background-color : white");
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
