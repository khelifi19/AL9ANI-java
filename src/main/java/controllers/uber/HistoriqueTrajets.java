package controllers.uber;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modeles.uber.Course;
import service.uber.CourseDAO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HistoriqueTrajets {


    @FXML
    private Button tRetour;


    @FXML
    private Button ajouterBtn;



    @FXML
    private Button tAncienne;

    @FXML
    private Button tCourseEnCours;
    private List<Course> historique;

    @FXML
    private  HBox cardLayoot;
    private CourseDAO courseDAO;

    public HistoriqueTrajets() {
        this.courseDAO = new CourseDAO();
    }

    @FXML
    public void initialize() {

        tRetour.setOnAction(event -> redirectToAccueil());
        refreshCardLayout();
    }

    private List<Course> historique() {
        return courseDAO.findHistoricalCourses();
    }


    private void refreshCardLayout() {
        cardLayoot.getChildren().clear();
        List<Course> historique = historique();
        for (Course course : historique) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/uber/front/Course/card2.fxml"));
                HBox carBox = fxmlLoader.load();
                Card cardController = fxmlLoader.getController();
                cardController.setData(course);
                cardLayoot.getChildren().add(carBox);
                carBox.getProperties().put("controller", cardController);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setupActionButtonsCellFactory();
    }

    private void setupActionButtonsCellFactory() {
        for (int i = 0; i < cardLayoot.getChildren().size(); i++) {
            HBox cardBox = (HBox) cardLayoot.getChildren().get(i);
            Card cardController = (Card) cardBox.getProperties().get("controller");
            if (cardController != null) {
          

              
                cardController.getBtnRereservez().setOnAction(event -> {

         
                    Course course = cardController.getCourse();

                    redirectToReservation(course);
                });
            }
        }
    }



    private boolean showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    private void redirectToAccueil() {
        System.out.println("Redirection vers la page d'accueil...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/homeUber.fxml"));
            Parent root = loader.load();
            HomeUber accueilController = loader.getController();
            if (accueilController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d'accueil.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (tRetour != null) {
                stage = (Stage) tRetour.getScene().getWindow();
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

        private void redirectToCourseEnCours() {
            System.out.println("Redirection vers la page des courses en cours...");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/coursesEnCours.fxml"));
                Parent root = loader.load();
                CourseEnCours courseEnCoursController = loader.getController();
                if (courseEnCoursController == null) {
                    System.out.println("Erreur: Impossible de charger le contrôleur de la page des courses en cours.");
                    return;
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) tCourseEnCours.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                System.out.println("Redirection réussie !");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la redirection: " + e.getMessage());
            }
        }

        private void redirectToHistoriqueCourses() {
            System.out.println("Redirection vers la page de l'historique des courses...");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/historiqueTrajets.fxml"));
                Parent root = loader.load();
                HistoriqueTrajets historiqueTrajetsController = loader.getController();
                if (historiqueTrajetsController == null) {
                    System.out.println("Erreur: Impossible de charger le contrôleur de la page de l'historique des courses.");
                    return;
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) tAncienne.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                System.out.println("Redirection réussie !");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la redirection: " + e.getMessage());
            }
        }

    private void redirectToReservation(Course course) {
        System.out.println("Redirection vers la page de réservation...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/reservation.fxml"));
            Parent root = loader.load();
            ReservationCourse reservationController = loader.getController();
            if (reservationController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de réservation.");
                return;
            }
            if (course != null) {
                reservationController.initializeFieldsWithCourse(course);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) ajouterBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }
    }

