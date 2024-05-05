package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modeles.user.UserModel;
import service.user.OtpManager;
import service.user.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class resetPassword {
    @FXML
    private Button loginP;

    @FXML
    private Button tryAgain;
    @FXML
    private PasswordField OnePassword;

    @FXML
    private ImageView back;

    @FXML
    private Label error_labelS;
    @FXML
    private Button validate;
    @FXML
    private TextField Email;
    @FXML
    private TextField mailfieldconfirm;
    @FXML
    private TextField mailfieldlast;
    @FXML
    private TextField mailfield;
    @FXML
    private ImageView arrow;

    @FXML
    private Button sending;
    private final UserService userS = new UserService();
    private  UserModel userM = new UserModel();



    private String email;
public void setFinal(String email){
    if (mailfieldlast != null) {
        System.out.println("i want to be here");
        mailfieldlast.setText(email);}
}
    public void setEmail(String email) {
        this.email = email;
        // Set the email value to the mailfieldconfirm TextField
        if (mailfieldconfirm != null ) {
            mailfieldconfirm.setText(email);
        } else if (mailfieldlast !=null) {
            System.out.println("i want to be here");
            mailfieldlast.setText(email);
        } else if (mailfield != null) {
            mailfield.setText(email);


        }
    }
    @FXML
    private void sendOTP(ActionEvent event) {
        String emailText = Email.getText();





        if (userS.checkIfEmailExists(emailText)){

            String otp = OtpManager.generateOTP(emailText);

            sendOtpEmail(emailText,otp);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/resetPassword.fxml"));



            Parent root = null;
            try {
                root = loader.load();
                resetPassword resetPasswordController = loader.getController();
                resetPasswordController.setEmail(emailText);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the scene to the primary stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("error");
            stage.show();

        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/error.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the scene to the primary stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("error");
            stage.show();
        }

    }
    public void  switchForgot(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/forgotPassword.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set the scene to the primary stage
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignUp");
        stage.show();
    }

    public void  switchSignUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/signup.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set the scene to the primary stage
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignUp");
        stage.show();
    }


    // Method to handle resetting the password
    @FXML
    private void update(ActionEvent event) {
        String mail =mailfield.getText();
        String otp = OnePassword.getText();
        System.out.println(mailfield.getText());
        System.out.println(otp);
       // System.out.println(OtpManager.verifyOTP(mail,otp));

        // Verify the one-time password
        if (OtpManager.verifyOTP(mail,otp)) {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/reset.fxml"));

            Parent root = null;
            try {
                root = loader.load();
                resetPassword resetPasswordController = loader.getController();
                resetPasswordController.setFinal(mail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the scene to the primary stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("update");
            stage.show();


        } else {
            error_labelS.setVisible(true);
            error_labelS.setText("The One Time Password you provided is invalid ");
        }
    }
    @FXML
    private void done(ActionEvent event) {
     String last=mailfieldlast.getText();
        System.out.println(mailfieldlast.getText());
        try {
            userM=userS.readUserE(last);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            try {
        userS.update(userM);


    }catch (SQLException e){
        throw new RuntimeException(e);
    }
        if (userM.getUsername().equals("admin")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminD/dashboard.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));

            primaryStage.initStyle(StageStyle.UNDECORATED);



            primaryStage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
            return;
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/profilesetting.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProfileSetting profileSetting = loader.getController();
            try {
                profileSetting.setUserInformation(userM.getUsername());
            } catch (SQLException e) {
                System.err.println(e);
            }
            // Set the scene to the primary stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
}
}

        private void sendOtpEmail(String recipientEmail, String otp) {
            String subject = "Your One-Time Password";
           // String body = "Your One-Time Password is: " + otp;
            String body = generateHtmlEmail(otp);

            sendEmail(recipientEmail, subject, body);
        }
    private String generateHtmlEmail(String otp) {
        // Read the HTML template file
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/main/resources/user/emailTemplate.html"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error reading HTML file: " + e.getMessage());
            return ""; // Return an empty string if there's an error
        }
        String template = contentBuilder.toString();

        // Replace placeholder with OTP
        String htmlContent = template.replace("<strong> otp </strong>", "<strong>" + otp + "</strong>");

        return htmlContent;
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
    }}
