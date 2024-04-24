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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.user.UserModel;
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

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

    private final UserService us = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<UserModel> users;
        btnSignout.setOnAction(new EventHandler<ActionEvent>() {
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
        try {
            users = us.read();
            for (int i = 0; i < users.size(); i++) {
                final int j = i;
                HBox node;
                try {
                    node = FXMLLoader.load(getClass().getResource("/view/user/adminD/Item.fxml"));
                    setUserInformation(node, users.get(i).getUsername(), users.get(i).getEmail(), users.get(i).getFirstName(), users.get(i).getLastName());
                    pnItems.getChildren().add(node);

                    // Give the items some effect
                    node.setOnMouseEntered(event -> {
                        node.setStyle("-fx-background-color : #ff374d");
                    });
                    node.setOnMouseExited(event -> {
                        node.setStyle("-fx-background-color : #02030A");
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
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
    }

    // Set User Information
    private void setUserInformation(HBox node, String username, String email, String firstName, String lastName) {
        Label usename = (Label) node.lookup("#usename");
        Label f_name = (Label) node.lookup("#f_name");
        Label l_name = (Label) node.lookup("#l_name");
        Label userEmail = (Label) node.lookup("#email");

        usename.setText(username);
        userEmail.setText(email);
        f_name.setText(firstName);
        l_name.setText(lastName);
    }
}
