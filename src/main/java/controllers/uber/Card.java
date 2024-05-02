package controllers.uber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import modeles.uber.Course;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Card {
    @FXML
    private HBox box;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnReservez;

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

    private String[] colors;
    private Course course;

    {
        colors = new String[]{"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};
    }


    public void setData(Course course)
    {
        this.course = course;
        // Supposons que vous ayez une instance de Course appelée "course"
        LocalDateTime date = course.getDate();
// Maintenant, formatez la date selon vos besoins
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Par exemple, format "jour/mois/année heure:minute"
        String formattedDate = date.format(formatter);
// Définissez le texte du label avec la date formatée
        tDate.setText(formattedDate);
        tDepart.setText(course.getDepart());
        tDestination.setText(course.getDestination());
        double prix = course.getPrix();
        String prixFormate = String.format("%.2f", prix);
        tPrix.setText(prixFormate);

        int nbPersonne = course.getNbPersonne();
        String nbPersonneString = String.valueOf(nbPersonne);

        tNbPersonne.setText(nbPersonneString);
        box.setStyle("-fx-background-color: #" + colors[(int) (Math.random() * colors.length)] + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0), 10,0,0,10);");



    }

    public Button getBtnSupprimer() {
        return btnAnnuler;
    }

    public Button getBtnModifier() {
        return btnModifier;
    }

    public Button getBtnRereservez() {
        return btnReservez;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
