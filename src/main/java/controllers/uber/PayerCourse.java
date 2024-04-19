package controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modeles.Course;
import service.CourseDAO;

import java.io.IOException;


public class PayerCourse {


    @FXML
    private Button tRetour;
        @FXML
        private Button btnAnnuler;

        @FXML
        private Button btnModifier;

        @FXML
        private Button btnPayer;



        @FXML
        private Button tAjouter;

        @FXML
        private Button tAncienne;

        @FXML
        private Button tCourseEnCours;

        @FXML
        private Label tDate;

        @FXML
        private Label tDepart;

        @FXML
        private Label tDestination;

        @FXML
        private Label tNbPersonne;

        @FXML
        private Label tPrix;

        private Course course;
        private CourseDAO courseDAO;
      public   PayerCourse(){
              this.courseDAO=new CourseDAO();
      }
        @FXML
        public void initialize() {


            tRetour.setOnAction(event -> redirectToAccueil());

                // Définir les actions des boutons
                btnAnnuler.setOnAction(event -> annulerCourse());
                btnModifier.setOnAction(event -> modifierCourse());
                btnPayer.setOnAction(event -> payerCourse());
            tCourseEnCours.setOnAction(event -> redirectToCourseEnCours());
            tAncienne.setOnAction(event -> redirectToHistoriqueCourses());
        tAjouter.setOnAction(event -> redirectToReservation());
        }

        public void setCourse(Course course) {
                this.course = course;
                afficherDetailsCourse();
        }

        private void afficherDetailsCourse() {
                tDate.setText(course.getDate().toString());
                tDepart.setText(course.getDepart());
                tDestination.setText(course.getDestination());

                tNbPersonne.setText(String.valueOf(course.getNbPersonne()));
                tPrix.setText(String.valueOf(course.getPrix()));
        }
        private void annulerCourse() {
                // Afficher une boîte de dialogue de confirmation
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText(null);
                confirmationDialog.setContentText("Êtes-vous sûr de vouloir annuler cette course ?");
                confirmationDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                                // L'utilisateur a confirmé la suppression
                                courseDAO.delete(course.getId()); // Supprimer la course de la base de données
                                // Afficher un message de confirmation
                                showAlert(Alert.AlertType.INFORMATION, "Confirmation", "La course a été annulée avec succès.");

                        }
                });
        }


        private void modifierCourse() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/modifier.fxml"));
                        Parent root = loader.load();
                        ModifierCourse modifierCourseController = loader.getController();
                        modifierCourseController.setCourseAModifier(course); // Passer les détails de la course
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void payerCourse() {
                // Ajouter la logique pour passer au paiement
        }

        private void showAlert(Alert.AlertType type, String title, String content) {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait();
        }
        public Course getCourse() {
              return   this.course ;
        }

    private void redirectToAccueil() {
        System.out.println("Redirection vers la page d'accueil...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/homeUber.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/coursesEnCours.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/historiqueTrajets.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/reservation.fxml"));
            Parent root = loader.load();
            ReservationCourse reservationController = loader.getController();
            if (reservationController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de réservation.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) tAjouter.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }

    }


