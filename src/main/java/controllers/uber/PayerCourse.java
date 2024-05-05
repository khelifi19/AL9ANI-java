package controllers.uber;

import com.stripe.exception.StripeException;



import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import modeles.uber.Course;
import service.uber.CourseDAO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;


import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;



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




                // Définir les actions des boutons
                btnAnnuler.setOnAction(event -> annulerCourse());
                btnModifier.setOnAction(event -> modifierCourse());
                btnPayer.setOnAction(event -> payerCourse());
            tRetour.setOnAction(event -> redirectToAccueil());

        }

        public void setCourse(Course course) {
                this.course = course;
            System.out.println(course.getId());
                afficherDetailsCourse();
        }

        private void afficherDetailsCourse() {
                tDate.setText(course.getDate().toString());
                tDepart.setText(course.getDepart());
                tDestination.setText(course.getDestination());

                tNbPersonne.setText(String.valueOf(course.getNbPersonne()));
                tPrix.setText(String.valueOf(course.getPrix()));
        }
    private void annulerCourse( ) {

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir annuler cette course ?");
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {

                    // L'utilisateur a confirmé la suppression
                    courseDAO.delete(course.getId()); // Supprimer la course de la base de données en utilisant l'ID passé en paramètre
                    // Afficher un message de confirmation
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "La course a été annulée avec succès.");
                    redirectToCourseEnCours();

                } catch (Exception e) {
                    // Gérer les erreurs survenues lors de la suppression de la course
                    e.printStackTrace();
                    // Afficher un message d'erreur à l'utilisateur ou effectuer d'autres actions nécessaires
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'annulation de la course.");
                }
            }
        });
    }



    private void modifierCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/modifier.fxml"));
            Parent root = loader.load();
            ModifierCourse modifierCourseController = loader.getController();
            modifierCourseController.setCourseAModifier(course); // Passer les détails de la course

            // Remplacer le contenu de la fenêtre principale
            Stage stage = (Stage) tRetour.getScene().getWindow(); // Utilisez n'importe quel nœud de votre scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Stage stage = (Stage) tAjouter.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }


    @FXML
    private void payerCourse() {
        try {
            // Récupérez les informations de la course
            String idCourse = String.valueOf(course.getId());
            double montant = course.getPrix();
            if (!isInternetAvailable()) {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Vérifiez votre connexion Internet et réessayez.");
                return;
            }
            // Créez une session de paiement Stripe
            Stripe.apiKey = "sk_test_51OpC2JB8wz5FUa8gcFWvnDXpkh0WAnmPUCBW72tJUXvj1nMSQEewEbJ1d9h3UXBbjgm2JtJ6zfdOzDmNjUOFCr2O00RcotKHJt";
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://t3.ftcdn.net/jpg/03/14/30/00/360_F_314300005_uZOXHOY3rLnfGISg47qOQKBFyb8ZXb0I.jpg")
                    .setCancelUrl("https://c8.alamy.com/compfr/echyhc/timbres-en-caoutchouc-rouge-non-valide-sur-un-fond-blanc-echyhc.jpg")
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("EUR")
                                                    .setUnitAmount((long) (montant * 100)) // Montant en cents (par exemple, 10,00 €)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Course " + idCourse) // Nom de la course
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            // Redirigez l'utilisateur vers l'URL de paiement
            redirectToPayment(session.getUrl());
        } catch (StripeException e) {
            // Gérer les erreurs de paiement

            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de paiement", "Une erreur est survenue lors du paiement.");
            // Supprimer la course en cas d'erreur de paiement
            courseDAO.delete(course.getId());
        }
    }


    // Méthode pour gérer la redirection vers l'URL de paiement
    private void redirectToPayment(String paymentUrl) {
        // Vérifier la disponibilité de la connexion Internet
        if (!isInternetAvailable()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Vérifiez votre connexion Internet et réessayez.");
            // Supprimer la course en cas d'échec de connexion
            courseDAO.delete(course.getId());
            return;
        }

        // Créer un WebView et un WebEngine pour afficher l'URL de paiement
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Charger l'URL de paiement dans le WebEngine
        try {
            URL url = new URL(paymentUrl);
            webEngine.load(url.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        // Créer une nouvelle scène pour afficher le WebView
        Scene scene = new Scene(webView, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Paiement");
        stage.show();

        // Gérer le chargement de la page
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.FAILED) {
                // Afficher une alerte en cas d'échec de chargement de la page
                showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger la page de paiement.");
            } else if (newState == Worker.State.SUCCEEDED) {
                // Vérifier si l'URL de la page est celle du `successUrl`
                String currentUrl = webEngine.getLocation();

                if (currentUrl.startsWith("https://t3.ftcdn.net/jpg/03/14/30/00/360_F_314300005_uZOXHOY3rLnfGISg47qOQKBFyb8ZXb0I.jpg")) {
                    // Le paiement a réussi, afficher un message de succès
                    showAlert(Alert.AlertType.INFORMATION, "Paiement réussi", "Le paiement a été effectué avec succès.");
                    // Ajout de la nouvelle course dans la base de données
                    courseDAO.save(course);

                    // Envoyer un e-mail au client avec les détails de la course
                    sendPaymentConfirmationEmail();
                    // Rediriger vers la page des cours en cours
                    redirectToCoursesInProgress();
                } else if (currentUrl.startsWith("https://c8.alamy.com/compfr/echyhc/timbres-en-caoutchouc-rouge-non-valide-sur-un-fond-blanc-echyhc.jpg")) {
                    // Le paiement a échoué ou a été annulé
                    showAlert(Alert.AlertType.ERROR, "Erreur de paiement", "Le paiement a échoué ou a été annulé.");
                    redirectToCoursesInProgress();
                }
            }
        });
    }

    private boolean isInternetAvailable() {
        try {
            // Créer une connexion avec une URL bien connue (par exemple, google.com)
            URL url = new URL("https://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect(); // Tenter de se connecter à l'URL
            connection.disconnect(); // Déconnecter la connexion après vérification

            // Si la connexion a réussi, la connexion Internet est disponible
            return true;
        } catch (IOException e) {
            // En cas d'erreur, la connexion Internet n'est pas disponible
            return false;
        }
    }


    // Redirection vers la vue des cours en cours si le paiement est effectué avec succès
    private void redirectToCoursesInProgress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/front/Course/coursesEnCours.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnPayer.getScene().getWindow(); // Remplacez payButton par le bouton utilisé pour démarrer le paiement
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de la redirection vers les cours en cours : " + e.getMessage());

        }
    }

    private void sendPaymentConfirmationEmail() {
        // Récupérer les détails de la course
        String courseDetails = "Détails de la course :\n" +
                "Destination: " + course.getDestination() + "\n" +
                "Départ: " + course.getDepart() + "\n" +
                "Départ: " + course.getDate() + "\n" +
                "Matricule de la voiture: " + course.getVoiture().getMatricule() + "\n" +
                " Num Chauffeur: " + course.getVoiture().getChauffeur().getNumero();


        // Envoyer un e-mail au client
        String clientEmail = "yassine.khelifi@esprit.tn"; // Remplacez par l'e-mail du client
        String subject = "Confirmation de paiement pour votre course";
        String body = "Bonjour,\n\nNous vous confirmons que le paiement pour votre course a été effectué avec succès.\n\n" + courseDetails + "\n\nCordialement,\nVotre équipe de réservation de courses.";

        // Envoyer l'e-mail
        sendEmail(clientEmail, subject, body);
    }


    public static void sendEmail(String recipientEmail, String subject, String body) {
        // Paramètres SMTP du serveur d'envoi d'e-mails
        String host = "smtp.gmail.com"; // Remplacez par le serveur SMTP de votre fournisseur de messagerie
        String username = "yassinkhelefi@gmail.com"; // Remplacez par votre adresse e-mail
        String password = "bnlynnqtdwuyosek"; // Remplacez par votre mot de passe

        // Propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");


        // Création d'une session SMTP avec authentification
        javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Envoi du message
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès !");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

}


