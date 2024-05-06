package controllers.front.reservation;



import utils.etatAct.*;
import modeles.etaAct.*;
import service.etaAct.*;
import controllers.front.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public DatePicker dateDP;
    @FXML
    public TextField nombreDePersonnesTF;

    @FXML
    public ComboBox<Etablissement> etablissementCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reservation currentReservation;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Etablissement etablissement : EtablissementService.getInstance().getAll()) {
            etablissementCB.getItems().add(etablissement);
        }

        currentReservation = ShowAllController.currentReservation;

        if (currentReservation != null) {
            topText.setText("Modifier reservation");
            btnAjout.setText("Modifier");

            try {
                dateDP.setValue(currentReservation.getDate());
                nombreDePersonnesTF.setText(String.valueOf(currentReservation.getNombreDePersonnes()));

                etablissementCB.setValue(currentReservation.getEtablissement());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reservation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Reservation reservation = new Reservation();
            reservation.setDate(dateDP.getValue());
            reservation.setNombreDePersonnes(Integer.parseInt(nombreDePersonnesTF.getText()));

            reservation.setEtablissement(etablissementCB.getValue());

            if (currentReservation == null) {
                if (ReservationService.getInstance().add(reservation)) {
                    AlertUtils.makeSuccessNotification("Reservation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reservation.setId(currentReservation.getId());
                if (ReservationService.getInstance().edit(reservation)) {
                    AlertUtils.makeSuccessNotification("Reservation modifié avec succés");
                    ShowAllController.currentReservation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }

        if (dateDP.getValue().isBefore(LocalDate.now())){
            AlertUtils.makeInformation("Date doit etre superieure a la date d'aujourdhui");
            return false;
        }

        if (nombreDePersonnesTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nombreDePersonnes ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(nombreDePersonnesTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("nombreDePersonnes doit etre un nombre");
            return false;
        }

        if (etablissementCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un etablissement");
            return false;
        }
        return true;
    }
}