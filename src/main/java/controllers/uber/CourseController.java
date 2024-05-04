package controllers.uber;

import controllers.user.dashboardController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import modeles.uber.Course;
import modeles.uber.Voiture;
import service.uber.CourseDAO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class CourseController {

        @FXML
        private Button btnCalender;
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
        private Button btnOverview;

        @FXML
        private ToggleButton btnVehicule;
        @FXML
        private ToggleButton btnChauffeur;

        @FXML
        private TableColumn<Course, Void> tActions;

        @FXML
        private TableColumn<Course, LocalDateTime> tDate;

        @FXML
        private TableColumn<Course, String> tDepart;

        @FXML
        private TableColumn<Course, String> tDestination;

        @FXML
        private TableColumn<Course, Integer> tNbPersonne;
        @FXML
        private Button btnRetour;

        @FXML
        private TableColumn<Course, Double> tPrix;

        @FXML
        private TableColumn<Course, Voiture> tVoiture;

        @FXML
        private TableView<Course> tableViewCourses;

        private CourseDAO courseDAO;



        private static final String ACCOUNT_SID = "AC35b06b363e3a08b12c028d73d80eb716";
        private static final String AUTH_TOKEN = "262546c4e828d69e6ee4f953a1c79054";
        private static final String TWILIO_NUMBER = "+17853846782";
        static {
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }
        public CourseController() {
                this.courseDAO = new CourseDAO();
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
        private void initialize() {
                initializeTableView();
                setupActionButtonsCellFactory();

                btnRetour.setOnAction(event -> redirectToAccueil());
                btnCalender.setOnAction(event->redirectToCalender());
        }

        private void initializeTableView() {
                tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                tDepart.setCellValueFactory(new PropertyValueFactory<>("depart"));
                tDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
                tNbPersonne.setCellValueFactory(new PropertyValueFactory<>("nbPersonne"));
                tPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
                tVoiture.setCellValueFactory(new PropertyValueFactory<>("voiture"));

                tVoiture.setCellFactory(column -> {
                        return new TableCell<Course, Voiture>() {
                                @Override
                                protected void updateItem(Voiture voiture, boolean empty) {
                                        super.updateItem(voiture, empty);

                                        if (voiture == null || empty) {
                                                setText(null);
                                        } else {
                                                setText(voiture.getMatricule());
                                        }
                                }
                        };
                });



                tableViewCourses.setItems(FXCollections.observableArrayList(courseDAO.findAll()));
        }

        private void setupActionButtonsCellFactory() {
                tActions.setCellFactory(param -> new TableCell<Course, Void>() {
                        private final Button supprimerButton = new Button();
                        private final ImageView supprimerIcon = new ImageView(new Image(getClass().getResourceAsStream("/uber/dash/Img/supprimer.png")));

                        {
                                // Définir l'image de l'icône et la taille
                                supprimerIcon.setFitWidth(20);
                                supprimerIcon.setFitHeight(20);
                                // Définir l'icône comme graphique du bouton
                                supprimerButton.setGraphic(supprimerIcon);

                                // Définir le comportement du bouton "Supprimer"
                                supprimerButton.setOnAction(event -> {
                                        Course course = getTableView().getItems().get(getIndex());
                                        boolean confirmDelete = showAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer cette Course ?");
                                        if (confirmDelete) {
                                                courseDAO.delete(course.getId());
                                                rafraichirTableView();
                                                System.out.println("Course supprimée : " + course);



                                           String clientPhoneNumber = "+21653360028";

                                                // Envoyer un SMS au client
                                                sendSMS(clientPhoneNumber, "Votre course pour " +course.getDestination() +" a été annulée.");
                                        }
                                });
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                        setGraphic(null);
                                } else {
                                        setGraphic(supprimerButton);
                                }
                        }
                });
        }



        private boolean showAlert(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                Optional<ButtonType> result = alert.showAndWait();
                return result.isPresent() && result.get() == ButtonType.OK;
        }

        private void rafraichirTableView() {
                tableViewCourses.setItems(FXCollections.observableArrayList(courseDAO.findAll()));
        }


        private void redirectToVoiture() {
                System.out.println("Redirection vers l accueil...");

                try {
                        FXMLLoader loader;
                        loader = new FXMLLoader(getClass().getResource("/uber/dash/voiture/voiture.fxml"));
                        Parent root = loader.load();
                        VoitureController  voitureController = loader.getController();
                        if (voitureController == null) {
                                System.out.println("Erreur: Impossible de charger le contrôleur de la page de voiture.");
                                return;
                        }

                        Scene scene = new Scene(root);
                        Stage stage;
                        if ( btnVehicule != null) {
                                stage = (Stage)  btnVehicule.getScene().getWindow();
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
        }

        private void redirectToChauffeur() {
                System.out.println("Redirection vers l accueil...");

                try {
                        FXMLLoader loader;
                        loader = new FXMLLoader(getClass().getResource("/uber/dash/chauffeur/chauffeur.fxml"));
                        Parent root = loader.load();
                        ChauffeurController chauffeurController = loader.getController();
                        if (chauffeurController == null) {
                                System.out.println("Erreur: Impossible de charger le contrôleur de la page de course.");
                                return;
                        }

                        Scene scene = new Scene(root);
                        Stage stage;
                        if ( btnChauffeur != null) {
                                stage = (Stage)  btnChauffeur.getScene().getWindow();
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
        }
        private void redirectToAccueil() {
                System.out.println("Redirection vers la page d'accueil...");

                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
                        Parent root = loader.load();
                        AdminController accueilController = loader.getController();
                        if (accueilController == null) {
                                System.out.println("Erreur: Impossible de charger le contrôleur de la page d'accueil.");
                                return;
                        }

                        Scene scene = new Scene(root);
                        Stage stage;
                        if (btnRetour != null) {
                                stage = (Stage) btnRetour.getScene().getWindow();
                        } else {
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
        }


        // Méthode pour envoyer un SMS via Twilio
        private void sendSMS(String toPhoneNumber, String messageBody) {
                try {
                        Message message = Message.creator(
                                new com.twilio.type.PhoneNumber(toPhoneNumber),
                                new com.twilio.type.PhoneNumber(TWILIO_NUMBER),
                                messageBody
                        ).create();

                        System.out.println("Message SID: " + message.getSid());
                } catch (Exception e) {
                        System.out.println("Erreur lors de l'envoi du SMS: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private void redirectToCalender() {


                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/calender.fxml"));
                        Parent root = loader.load();
                        CalenderController calenderController= loader.getController();
                        if (calenderController == null) {
                                System.out.println("Erreur: Impossible de charger le contrôleur de la page  des courses .");
                                return;
                        }

                        Scene scene = new Scene(root);
                        Stage stage;
                        if (btnCalender != null) {
                                stage = (Stage) btnCalender.getScene().getWindow();
                        }  else {
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
        }
}
