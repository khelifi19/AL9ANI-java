package controllers.uber;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeUber {


    @FXML
    private Button tAdmin;

    @FXML
    private Button btnCoursesEnCours;

    @FXML
    private Button btnHistorique;

    @FXML
    private Button btnReservez;

    @FXML
    private Button tAjouter;

    @FXML
    private Button tAncienne;

    @FXML
    private Button tCourseEnCours;


    @FXML
    private void initialize() {
        btnReservez.setOnAction(event -> redirectToReservation());
       tAjouter.setOnAction(event -> redirectToReservation());
        btnCoursesEnCours.setOnAction(event->redirectToCoursesEnCours());
       tCourseEnCours.setOnAction(event->redirectToCoursesEnCours());
        btnHistorique.setOnAction(event->redirectToHistoriqueCourses());
        tAncienne.setOnAction(event->redirectToHistoriqueCourses());
        tAdmin.setOnAction(event->redirectToDashboard());

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
            Stage stage=null;
            if (btnReservez != null) {
                stage = (Stage) btnReservez.getScene().getWindow();
            } else if (tAjouter !=null) {
              stage = (Stage) tAjouter.getScene().getWindow();

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


    private void redirectToCoursesEnCours() {
        System.out.println("Redirection vers la page de courses en cours...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/coursesEnCours.fxml"));
            Parent root = loader.load();
            CourseEnCours courseEnCoursController = loader.getController();
            if (courseEnCoursController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de courses en cours.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (btnCoursesEnCours != null) {
                stage = (Stage) btnCoursesEnCours.getScene().getWindow();
            } else if (tCourseEnCours != null) {
                stage = (Stage) tCourseEnCours.getScene().getWindow();
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

    private void redirectToHistoriqueCourses() {
        System.out.println("Redirection vers la page de historique Courses...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/historiqueTrajets.fxml"));
            Parent root = loader.load();
            HistoriqueTrajets historiqueTrajetsController= loader.getController();
            if (historiqueTrajetsController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de historique des courses .");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (btnHistorique != null) {
                stage = (Stage) btnHistorique.getScene().getWindow();
            } else if (tAncienne != null) {
                stage = (Stage) tAncienne.getScene().getWindow();
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


 private void redirectToDashboard() {
        System.out.println("Redirection vers le dashboard...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
            Parent root = loader.load();
            AdminController adminController = loader.getController();
            if (adminController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d admin.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (tAdmin != null) {
                stage = (Stage) tAdmin.getScene().getWindow();
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



    }






