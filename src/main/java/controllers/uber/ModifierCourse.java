package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modeles.Course;
import modeles.Voiture;
import service.CourseDAO;
import service.VoitureDAO;

import java.io.IOException;
import java.time.LocalDate;

public class ModifierCourse {


    @FXML
    private Button tRetour;
    @FXML
    private Button ajouterBtn;

    @FXML
    private Label errorDate;

    @FXML
    private Label errorDepart;

    @FXML
    private Label errorDestination;

    @FXML
    private Label errorPersonne;

    @FXML
    private Button tAncienne;

    @FXML
    private Button tConfirmer;

    @FXML
    private Button tCourseEnCours;

    @FXML
    private DatePicker tDate;

    @FXML
    private TextField tDepart;

    @FXML
    private ComboBox tDestination;

    @FXML
    private Spinner<Integer> tNbPersonne;


    private CourseDAO courseDAO;
    private VoitureDAO voitureDAO;
    private Course courseAModifier;
    private boolean modifiedFromCoursePage = false;

    public ModifierCourse() {
        this.courseDAO = new CourseDAO();
        this.voitureDAO = new VoitureDAO();
    }

    public void setCourseAModifier(Course course) {
        this.courseAModifier = course;
        afficherInformationsCourse();
    }

    private void afficherInformationsCourse() {
        if (courseAModifier != null) {
            tDate.setValue(courseAModifier.getDate().toLocalDate());
            tDepart.setText(courseAModifier.getDepart());
            tDestination.setValue(courseAModifier.getDestination());
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, courseAModifier.getNbPersonne());
            tNbPersonne.setValueFactory(valueFactory);
        }
    }

    @FXML
    private void initialize() {
        tNbPersonne.setOnMouseClicked(event -> tNbPersonne.requestFocus());
        // Set the value factory for the Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1);
        tNbPersonne.setValueFactory(valueFactory);
        tDestination.getItems().addAll("Kfc", "Plan b", "Baguette&Baguette", "33", "716");
        errorDate.setText("");
        errorDepart.setText("");
        errorDestination.setText("");
        errorPersonne.setText("");
        afficherInformationsCourse();
        tConfirmer.setOnAction(event -> confirmer());
        tRetour.setOnAction(event -> redirectToAccueil());
        tCourseEnCours.setOnAction(event -> redirectToCourseEnCours());
        tAncienne.setOnAction(event -> redirectToHistoriqueCourses());
        ajouterBtn.setOnAction(event -> redirectToReservation());
    }

    @FXML
    private void confirmer() {
        // Réinitialisation des messages d'erreur
        errorDate.setText("");
        errorDepart.setText("");
        errorDestination.setText("");
        errorPersonne.setText("");

        // Validation des champs
        LocalDate selectedDate = tDate.getValue();
        String depart = tDepart.getText().trim();
        String destination = tDestination.getValue().toString();
        int nbPassagers = Integer.parseInt(tNbPersonne.getValue().toString());

        // Vérification des champs
        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            errorDate.setText("Veuillez sélectionner une date valide dans le futur.");
            return;
        }

        if (depart.isEmpty()) {
            errorDepart.setText("Veuillez saisir un lieu de départ.");
            return;
        }

        if (destination.isEmpty()) {
            errorDestination.setText("Veuillez sélectionner une destination.");
            return;
        }

        if (nbPassagers < 1 || nbPassagers > 9) {
            errorPersonne.setText("Le nombre de passagers doit être compris entre 1 et 9.");
            return;
        }

        // Détermination du type de véhicule en fonction du nombre de passagers
        String typeVehicule = nbPassagers < 5 ? "Voiture" : "Bus";

        // Récupération de la voiture disponible
        Voiture voiture = voitureDAO.getAvailableVehicle(typeVehicule);
        if (voiture == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun véhicule disponible pour cette course.");
            return;
        }

        // Mettre à jour la course
        courseAModifier.setDate(selectedDate.atStartOfDay());
        courseAModifier.setDepart(depart);
        courseAModifier.setDestination(destination);
        courseAModifier.setNbPersonne(nbPassagers);
        courseAModifier.setVoiture(voiture);

        // Mettre à jour la course dans la base de données
        courseDAO.update(courseAModifier);

        // Affichage d'un message de succès
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "La course a été modifiée avec succès.");

        redirectToNextPage();
    }
    private void redirectToPaymentPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Course/payement.fxml"));
            Parent root = loader.load();
            PayerCourse paymentController = loader.getController();

            // Initialisation de l'objet course dans le contrôleur PayerCourse
            paymentController.setCourse(courseAModifier);

            Scene scene = new Scene(root);
            Stage stage = (Stage) tConfirmer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
            Stage stage = (Stage) ajouterBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }

    private void redirectToNextPage() {
        if (modifiedFromCoursePage) {
            redirectToCourseEnCours();
        } else {
            redirectToPaymentPage();
        }


    }
    public void setModifiedFromCoursePage(boolean value) {
        this.modifiedFromCoursePage = value;
    }
}
