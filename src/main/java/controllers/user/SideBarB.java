package controllers.user;

import controllers.evenement.EvenementControllerBack;
import controllers.reclamation.ReclamationController;
import controllers.reclamation.ReponseController;
import controllers.uber.AdminController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.user.UserService;
import utils.etatAct.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.al9ani.test.Main.mainStage;

public class SideBarB implements Initializable {
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnSignout;

    @FXML
    private Button etabB;

    @FXML
    private Button eventB;
    @FXML
    private Button act;
    @FXML
    private Button com;
    @FXML
    private Button recB;
    @FXML
    private Button res;
    @FXML
    private Button uberB;

    @FXML
    private Button userb;
    private final UserService us = new UserService();

    @FXML
    private void redirectTo(ActionEvent event) throws IOException {
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

                Stage ps = mainStage;
                ps.initStyle(StageStyle.UNDECORATED);
                ps.setScene(new Scene(ro));





                ps.show();

                break;

            case "uberB":
                try {

                    FXMLLoader ld = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
                    Parent root = ld.load();
                    AdminController homeUber = ld.getController();
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
                loadEtab();
             res.setVisible(true);
             etabB.setVisible(false);

                break;
            case "res": loadRese();
            res.setVisible(false);
            etabB.setVisible(true);
                break;
            case "act":
                loadpost();
                com.setVisible(true);
                etabB.setVisible(false);
                break;
            case "com":
                loadCOM();
                com.setVisible(false);
                etabB.setVisible(true);
                break;
            case "eventB":
                FXMLLoader le = new FXMLLoader(getClass().getResource("/evenement/EvenementBack.fxml"));
                Parent rot = null;
                try {
                    rot = le.load();
                    EvenementControllerBack dashboardController = le.getController();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage pis = mainStage;
                pis.setScene(new Scene(rot));





                pis.show();


                break;
            case "recB":

                FXMLLoader lr = new FXMLLoader(getClass().getResource("/reclamation/reclamation.fxml"));
                Parent r = lr.load();
                ReclamationController reclamationController = lr.getController();

                Scene sc = new Scene(r);
                Stage stage;
                stage = mainStage;
                stage.setScene(sc);
                stage.show();
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

                Stage primaryStage = mainStage;
                primaryStage.setScene(new Scene(root));





                primaryStage.show();

                break;
            default:
                 loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
                 root = null;
                try {
                    root = loader.load();
                    dashboardController dashboardController = loader.getController();
                    dashboardController.handleClicks(buttonId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                 primaryStage = mainStage;
                primaryStage.setScene(new Scene(root));





                primaryStage.show();
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        com.setVisible(false);
        etabB.setVisible(true);
        res.setVisible(false);
        etabB.setVisible(true);
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
    }




    public void loadBack() {
        loadScene(
                Constants.FXML_BACK_MAIN_WINDOW,
                ""
        );
    }
    public void loadEtab(){
        loadScene(
                Constants.FXML_BACK_DISPLAY_ALL_ETABLISSEMENT,
                ""
        );}
        public void loadRese(){
            loadScene(
                    Constants.FXML_BACK_DISPLAY_ALL_RESERVATION,
                    ""
            );


    }
    public void loadpost(){
        loadScene(
                Constants.FXML_BACK_DISPLAY_ALL_POST,
                ""
        );}
        public void loadCOM(){
            loadScene(
                    Constants.FXML_BACK_DISPLAY_ALL_COMMENTAIRE,
                    ""
            );


    }
   /* public void logout() {

        System.out.println("Deconnexion ..");
        loadLogin();
    }*/

    private void loadScene(String fxmlLink, String title) {
        try {
            Stage primaryStage = mainStage;


            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlLink))));
            scene.setFill(Color.TRANSPARENT);

            //primaryStage.getIcons().add(new Image("app/images/app-icon.png"));
            primaryStage.setTitle(title);
            primaryStage.setWidth(1286);
            primaryStage.setHeight(779);
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(700);
            primaryStage.setScene(scene);
            primaryStage.setX((Screen.getPrimary().getBounds().getWidth() / 2) - (1100 / 2.0));
            primaryStage.setY((Screen.getPrimary().getBounds().getHeight() / 2) - (700 / 2.0));

            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
