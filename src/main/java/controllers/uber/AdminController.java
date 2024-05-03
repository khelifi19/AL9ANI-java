package controllers.uber;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.Stage;


import java.io.IOException;

public class AdminController {

    @FXML
    private Button btnChauffeur;

    @FXML
    private Button btnCourse;

    @FXML
    private Button btnVehicule;

    @FXML
    private Button tChauffeur;

    @FXML
    private Button tCourse;

    @FXML
    private Button tRetour;

    @FXML
    private Button tVoiture;

    @FXML
    private Button btnCalender;


    @FXML
    private void initialize() {
        btnChauffeur.setOnAction(event -> redirectToChauffeur());
        btnVehicule.setOnAction(event -> redirectToVoiture());
       btnCourse.setOnAction(event->redirectToCourse());
        tCourse.setOnAction(event->redirectToCourse());
       tChauffeur.setOnAction(event->redirectToChauffeur());
        tVoiture.setOnAction(event->redirectToVoiture());
        tRetour.setOnAction(event->redirectToAcceuil());


    }



    private void redirectToChauffeur() {
        System.out.println("Redirection vers la page de gestion chauffeur...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/chauffeur/chauffeur.fxml"));
            Parent root = loader.load();
          ChauffeurController chauffeurController = loader.getController();
            if (chauffeurController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de gestion chauffeur.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage=null;
            if (btnChauffeur != null) {
                stage = (Stage) btnChauffeur.getScene().getWindow();
            } else if (tChauffeur !=null) {
                stage = (Stage) tChauffeur.getScene().getWindow();

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


    private void redirectToVoiture() {
        System.out.println("Redirection vers la page de voiture...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/voiture/voiture.fxml"));
            Parent root = loader.load();
            VoitureController voitureController = loader.getController();
            if (voitureController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de voiture.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (btnVehicule != null) {
                stage = (Stage) btnVehicule.getScene().getWindow();
            } else if (tVoiture != null) {
                stage = (Stage) tVoiture.getScene().getWindow();
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

    private void redirectToCourse() {
        System.out.println("Redirection vers la page de historique Courses...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/course/course.fxml"));
            Parent root = loader.load();
           CourseController courseController= loader.getController();
            if (courseController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page  des courses .");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (btnCourse != null) {
                stage = (Stage) btnCourse.getScene().getWindow();
            } else if (tCourse != null) {
                stage = (Stage) tCourse.getScene().getWindow();
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


    private void redirectToAcceuil() {
        System.out.println("Redirection vers l accueil...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/uber/front/Course/homeUber.fxml"));
            Parent root = loader.load();
          HomeUber homeUber = loader.getController();
            if (homeUber == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (tRetour != null) {
                stage = (Stage) tRetour.getScene().getWindow();
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
