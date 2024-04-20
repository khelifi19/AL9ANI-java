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

public class CourseEnCours {


    @FXML
    private Button tRetour;




    @FXML
    private Button ajouterBtn;



    @FXML
    private Button tAncienne;

    @FXML
    private Button tCourseEnCours;

    @FXML
     private  HBox cardLayoot;

    CourseDAO courseDAO;
private List<Course> enCours;

    public CourseEnCours() {
        this.courseDAO = new CourseDAO();
    }

    @FXML
    public void initialize() {
        tRetour.setOnAction(event -> redirectToAccueil());
        tCourseEnCours.setOnAction(event -> redirectToCourseEnCours());
        tAncienne.setOnAction(event -> redirectToHistoriqueCourses());
        ajouterBtn.setOnAction(event -> redirectToReservation());

        refreshCardLayout();
    }

    private List<Course> enCours() {
        return courseDAO.findCurrentCourses();
    }

    private void setupActionButtonsCellFactory() {
        for (int i = 0; i < cardLayoot.getChildren().size(); i++) {
            HBox cardBox = (HBox) cardLayoot.getChildren().get(i);
            Card cardController = (Card) cardBox.getProperties().get("controller");
            if (cardController != null) {
                cardController.getBtnSupprimer().setOnAction(event -> {
                    Course course = cardController.getCourse();
                    boolean confirmDelete = showAlert("Confirmation", "Êtes-vous sûr de vouloir annuler cette course ?");
                    if (confirmDelete) {
                        courseDAO.delete(course.getId());
                        refreshCardLayout();
                        System.out.println("Course annulée : " + course);
                    }
                });

                // Ajoutez l'action pour le bouton "Modifier"
                cardController.getBtnModifier().setOnAction(event -> {

                    // Obtenez la course associée à la carte
                    Course course = cardController.getCourse();
                    // Redirigez vers la page de modification de la course
                    redirectToModificationPage(course);
                });
            }
        }
    }

    private void refreshCardLayout() {
        cardLayoot.getChildren().clear();
        List<Course> enCours = enCours();
        for (Course course : enCours) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/uber/front/Course/card.fxml"));
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


    private void redirectToReservation() {
        System.out.println("Redirection vers la page de réservation...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/reservation.fxml"));
            Parent root = loader.load();
            ReservationCourse reservationController = loader.getController();
            if (reservationController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de réservation.");
                return;
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

    private void redirectToModificationPage(Course course) {
        System.out.println("Redirection vers la page de modification de la course...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/modifier.fxml"));
            Parent root = loader.load();
            ModifierCourse modifierCourseController = loader.getController();
            if (modifierCourseController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de modification de la course.");
                return;
            }

            modifierCourseController.setCourseAModifier(course); // Passage de la course à modifier au contrôleur de la page de modification
            modifierCourseController.setModifiedFromCoursePage(true);
            Scene scene = new Scene(root);
            Stage stage = (Stage) cardLayoot.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection vers la page de modification de la course réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection vers la page de modification de la course: " + e.getMessage());
        }
    }


}
