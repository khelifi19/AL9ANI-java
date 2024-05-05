package controllers.uber;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modeles.uber.Course;
import modeles.uber.Voiture;
import modeles.user.UserModel;
import service.uber.CourseDAO;
import service.uber.VoitureDAO;

import java.io.IOException;
import java.time.LocalDate;

public class ReservationCourse {


    @FXML
    private Button tRetour;
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

    public ReservationCourse() {
        this.courseDAO = new CourseDAO();
        this.voitureDAO = new VoitureDAO();
    }
    @FXML
    private void initialize() {
        tNbPersonne.setOnMouseClicked(event -> tNbPersonne.requestFocus());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1);
        tNbPersonne.setValueFactory(valueFactory);
        tDestination.getItems().addAll("Kfc", "Plan b", "Baguette&Baguette", "33", "716");
        tConfirmer.setOnAction(event -> confirmer());
        tRetour.setOnAction(event -> redirectToAccueil());
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
        String destination = (tDestination.getValue() != null) ? tDestination.getValue().toString() : "";
        Integer nbPassagers = (Integer) tNbPersonne.getValue(); // Vérifier également si c'est null

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

        if (nbPassagers == null || nbPassagers < 1 || nbPassagers > 9) {
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

        // Création de la nouvelle course
        Course nouvelleCourse = new Course();
        UserModel user=new UserModel();
        user.setId(53);
        nouvelleCourse.setDate(selectedDate.atStartOfDay());
        nouvelleCourse.setDepart(depart);
        nouvelleCourse.setDestination(destination);
        nouvelleCourse.setNbPersonne(nbPassagers);
  nouvelleCourse.setUser(user);

       float prix=50;
        float frais_passagers= (float) (nbPassagers*0.5);
        float frais_chauffeur=10;
        float frais_vehicule= typeVehicule == "Bus" ?50 :30;
        float total=frais_vehicule+frais_chauffeur+frais_passagers+prix;
        nouvelleCourse.setPrix(total);
        nouvelleCourse.setVoiture(voiture);



        // Redirection vers le paiement.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/payement.fxml"));
            Parent root = loader.load();
            PayerCourse paymentController = loader.getController();



// Initialisation de l'objet course dans le contrôleur PayerCourse
            paymentController.setCourse(nouvelleCourse);

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

    @FXML
    public void initializeFieldsWithCourse(Course course) {
        if (course != null) {
            tDate.setValue(course.getDate().toLocalDate());
            tDepart.setText(course.getDepart());
            tDestination.setValue(course.getDestination());
            tNbPersonne.getValueFactory().setValue(course.getNbPersonne());
        }
    }
}
