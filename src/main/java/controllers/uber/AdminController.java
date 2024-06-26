package controllers.uber;

import controllers.user.dashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.Stage;
import javafx.stage.StageStyle;



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

        tCourse.setOnAction(event->redirectToCourse());
       tChauffeur.setOnAction(event->redirectToChauffeur());
        tVoiture.setOnAction(event->redirectToVoiture());



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
