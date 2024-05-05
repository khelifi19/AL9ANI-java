package controllers.reclamation;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import controllers.uber.AdminController;
import controllers.uber.HomeUber;
import controllers.user.dashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.reclamation.*;
import modeles.reclamation.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.Font;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class ReclamationBackController implements Initializable {

    @FXML
    private ImageView IMG;

    @FXML
    private Button ReponseB;

    @FXML
    private Button btnOverview;

    @FXML
    private Button etabB;

    @FXML
    private Button eventB;

    @FXML
    private Label fruitNameLable11;

    @FXML
    private Label fruitNameLable111;

    @FXML
    private Button generatepdfid;

    @FXML
    private TextField objet;

    @FXML
    private Button recB;


    @FXML
    private TextField searchField;

    @FXML
    private TextField text;



    @FXML
    private Button uberB;

    @FXML
    private Button userb;

    private int sta;
    @FXML
    private ComboBox<String> triChoiceBox;
    @FXML
    private ListView<Reclamation> recl;

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
    private void redirectToAcceuil() {
        System.out.println("Redirection vers l accueil...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/reclamation/reponseback.fxml"));
            Parent root = loader.load();
            ReponsebackController homeUber = loader.getController();
            if (homeUber == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d accueil.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (ReponseB != null) {
                stage = (Stage) ReponseB.getScene().getWindow();
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
    public static void sendEmail(String recipientEmail, String subject, String body) {
        // Paramètres SMTP du serveur d'envoi d'e-mails
        String host = "smtp.gmail.com"; // Remplacez par le serveur SMTP de votre fournisseur de messagerie
        String username = "arijlaatigue01@gmail.com"; // Remplacez par votre adresse e-mail
        String password = "fcqljqftmfdlsbrn"; // Remplacez par votre mot de passe

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
            message.setContent(body, "text/html");


            // Envoi du message
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès !");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }
    private void sendOtpEmail(String recipientEmail) {
        String subject = "Claim received";
        String body = "Your claim is received ,one of our team will contact as soon as possible ";


        sendEmail(recipientEmail, subject, body);
    }
    @FXML
    void ajouter(ActionEvent event) {
        if (isValidInput()) {
            ReclamationService rs = new ReclamationService();
Reclamation r = new Reclamation(
        1,
        objet.getText(),
        text.getText(),
        this.sta
);
            rs.ajouter(r);
            sendOtpEmail("behagamer@gmail.com");
            // Clear and reload data after adding
            load();
        }
    }

    private boolean isValidInput() {
        if (objet.getText().isEmpty() || text.getText().isEmpty() ) {
            showAlert("Error", "Please fill in all the fields.");
            return false;
        }
        return true;
    }

    @FXML
    void delete(ActionEvent event) {
        ReclamationService rs = new ReclamationService();

        if (recl.getSelectionModel().getSelectedItem() != null) {
            rs.supprimer(recl.getSelectionModel().getSelectedItem());

            // Clear and reload data after deleting
            load();
        }
    }

    @FXML
    void update(ActionEvent event) {
        if (isValidInput() && recl.getSelectionModel().getSelectedItem() != null) {
            ReclamationService rs = new ReclamationService();

            rs.modifier(new Reclamation(
                    recl.getSelectionModel().getSelectedItem().getId(),
                    1,
                    objet.getText(),
                    text.getText(),
                    this.sta
            ));

            // Clear and reload data after updating
            load();
        }
    }

    int id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();
        ReponseB.setOnAction(event -> {redirectToAcceuil();});
    }
    @FXML
    void makePdf(ActionEvent event) {
        Reclamation selectedLivraison = recl.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            // Créer un document PDF
            Document document = new Document();
            try {
                // Chemin du fichier PDF à créer
                String filePath = "reclamation.pdf";

                // Créer le fichier PDF et associer le document à ce fichier
                PdfWriter.getInstance(document, new FileOutputStream(filePath));

                // Ouvrir le document pour y ajouter du contenu
                document.open();

                // Ajouter le titre centré avec une couleur bleue
                Paragraph title = new Paragraph("Reclamation", FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD, BaseColor.BLUE));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Ajouter un saut de ligne
                document.add(new Paragraph("\n"));

                // Ajouter l'ID de la livraison au contenu du PDF
                document.add(new Paragraph("ID Reclamation: " + selectedLivraison.getId()));

                // Ajouter du contenu au document PDF
                document.add(new Paragraph("Text: " + selectedLivraison.getText()));
                document.add(new Paragraph("Objet: " + selectedLivraison.getObjet()));

                // Fermer le document une fois que tout le contenu est ajouté
                document.close();

                //itemselcted.setText("PDF généré avec succès !");
                // Ouvrir automatiquement le fichier PDF   sayeee testii
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
               // itemselcted.setText("Erreur lors de la génération du PDF.");
            }
        }
    }


    @FXML
    void TrichoiceMethod(ActionEvent event) {
        if (triChoiceBox.getValue().equals("Text")) {
            Trietext();
        } else if (triChoiceBox.getValue().equals("Objet")) {
            TrieObjet();
        }
    }

    private void TrieObjet() {
        ReclamationService l = new ReclamationService();
        Reclamation ld = new Reclamation();
        List<Reclamation> list = l.triObjet();
        recl.getItems().setAll(list);

    }

    private void Trietext() {
        ReclamationService l = new ReclamationService();
        Reclamation ld = new Reclamation();
        List<Reclamation> list = l.triText();
        recl.getItems().setAll(list);
    }
    public void load() {
        // Clear the items in the ListView before adding new ones
        recl.getItems().clear();
        ObservableList<String> items = FXCollections.observableArrayList("Text", "Objet");

        // Définissez les éléments de la ChoiceBox en utilisant la liste observable
        triChoiceBox.setItems(items);

        ReclamationService rs = new ReclamationService();
        rs.afficher().forEach(reclamation -> {
            recl.getItems().add(reclamation);
        });

        recl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id = newValue.getId();
                objet.setText(newValue.getObjet());
                text.setText(newValue.getText());
            }
        });

        // Search functionality: Update the list based on search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Reclamation> filteredList = rs.search(newValue);  // Assuming rs.search() exists
            recl.getItems().setAll(filteredList);
        });

    }
    private String formatCustomDate(java.util.Date date) {
        // Convertir java.util.Date en java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        // Mettez ici votre logique de formatage personnalisé de la date
        // Par exemple : SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        // return formatter.format(sqlDate);
        return sqlDate.toString(); // Pour le moment, nous retournons simplement la date brute
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
