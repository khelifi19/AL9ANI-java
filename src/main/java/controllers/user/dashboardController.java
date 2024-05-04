package controllers.user;

import controllers.reclamation.ReclamationBackController;
import controllers.reclamation.ReponsebackController;
import controllers.uber.AdminController;
import controllers.uber.HomeUber;
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
import javafx.stage.StageStyle;
import modeles.user.UserModel;
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private Button Bact;

    @FXML
    private Button Betab;

    @FXML
    private Button Bevent;

    @FXML
    private Button Brecl;

    @FXML
    private Button Buber;

    @FXML
    private Button Buser;

    @FXML
    private Button btnPackages;

    @FXML
    private Button userb;

    @FXML
    private Button uberB;

    @FXML
    private Button etabB;

    @FXML
    private Button eventB;

    @FXML
    private Button recB;


    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlUsers;
    @FXML
    private TextField search;


    @FXML
    private Pane PnlPost;
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/signup.fxml"));
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
                    node = FXMLLoader.load(getClass().getResource("/user/adminD/Item.fxml"));
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
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the current displayed users
            pnItems.getChildren().clear();

            // Filter users based on the search input
            List<UserModel> filteredUsers = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getFirstName().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getLastName().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            // Display the filtered users
            for (UserModel user : filteredUsers) {
                HBox node;
                try {
                    totalU.setText(String.valueOf(filteredUsers.size()));
                    node = FXMLLoader.load(getClass().getResource("/user/adminD/Item.fxml"));
                    HBox.setHgrow(node, Priority.ALWAYS);
                    setUserInformation(node, user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
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
        });
    }
    @FXML
    private void redirectTo(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
System.out.println(buttonId);
        switch (buttonId) {
            case "userb" :

                FXMLLoader loa = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent ro = null;
                try {
                    ro = loa.load();
                    dashboardController dashboardController = loa.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage ps = new Stage();
                ps.setScene(new Scene(ro));

                ps.initStyle(StageStyle.UNDECORATED);



                ps.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;

            case "uberB":
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
                    Parent root = loader.load();
                    AdminController homeUber = loader.getController();
                    if (homeUber == null) {
                        System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                        return;
                    }

                    Scene scene = new Scene(root);
                    Stage stage;
                    if (uberB != null) {
                        stage = (Stage) uberB.getScene().getWindow();
                    }
                    else {
                        System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                        return;
                    }

                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Redirection réussie !");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de la redirection: " + e.getMessage());
                }

                break;
            case "etabB":

                break;
            case "eventB":
                // Redirection vers la page des événements
                // Exemple : goToEventPage();
                break;
            case "recB":
                // Redirection vers la page de recommandations
                // Exemple : goToRecommendationPage();
                break;
            case "btnOverview":
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                    dashboardController dashboardController = loader.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root));

                primaryStage.initStyle(StageStyle.UNDECORATED);



                primaryStage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;
            default:
                // Action par défaut si aucun cas ne correspond
                break;
        }
    }
    @FXML
    private void redirectToO(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        System.out.println(buttonId);
        switch (buttonId) {
            case "Buser" :

                FXMLLoader loa = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                Parent ro = null;
                try {
                    ro = loa.load();
                    dashboardController dashboardController = loa.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage ps = new Stage();
                ps.setScene(new Scene(ro));

                ps.initStyle(StageStyle.UNDECORATED);



                ps.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;

            case "Buber":
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
                    Parent root = loader.load();
                    AdminController homeUber = loader.getController();
                    if (homeUber == null) {
                        System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                        return;
                    }

                    Scene scene = new Scene(root);
                    Stage stage;
                    if (uberB != null) {
                        stage = (Stage) uberB.getScene().getWindow();
                    }
                    else {
                        System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                        return;
                    }

                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Redirection réussie !");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de la redirection: " + e.getMessage());
                }

                break;
            case "etabB":

                break;
            case "eventB":
                // Redirection vers la page des événements
                // Exemple : goToEventPage();
                break;
            case "Brecl":


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/reponseback.fxml"));
                    Parent root = loader.load();
                    ReponsebackController homeUber = loader.getController();

                    Scene scene = new Scene(root);
                    Stage stage;
                stage = (Stage) recB.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                break;
            case "Bact":




                break;
            default:
                // Action par défaut si aucun cas ne correspond
                break;
        }
    }
    public void handleClicks(String actionEvent) {
System.out.println(actionEvent);

        if (actionEvent.equals("userb")) {
pnlOverview.setVisible(false);
            pnlUsers.setVisible(true);

        }
        if (actionEvent.equals("btnOverview")) {
            pnlOverview.setVisible(true);
            pnlUsers.setVisible(false);

        }
        if (actionEvent.equals("Buser")) {
            pnlOverview.setVisible(false);
            pnlUsers.setVisible(true);

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
        dialogPane.getStylesheets().add(getClass().getResource("/user/alertStyle.css").toExternalForm());
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
                    node = FXMLLoader.load(getClass().getResource("/user/adminD/Item.fxml"));
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
