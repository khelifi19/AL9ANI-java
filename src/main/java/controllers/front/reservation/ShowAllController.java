package controllers.front.reservation;

import utils.etatAct.*;
import modeles.etaAct.*;
import service.etaAct.*;
import controllers.front.MainWindowController;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Reservation currentReservation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;


    List<Reservation> listReservation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReservation = ReservationService.getInstance().getAll();

        sortCB.getItems().addAll(
                "Tri par date",
                "Tri par nombre de personnes",
                "Tri par etablissement"
        );

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listReservation);

        if (!listReservation.isEmpty()) {
            for (Reservation reservation : listReservation) {
                mainVBox.getChildren().add(makeReservationModel(reservation));
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReservationModel(
            Reservation reservation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_RESERVATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + reservation.getDate());
            ((Text) innerContainer.lookup("#nombreDePersonnesText")).setText("NombreDePersonnes : " + reservation.getNombreDePersonnes());

            ((Text) innerContainer.lookup("#etablissementText")).setText("Etablissement : " + reservation.getEtablissement().toString());


            ((Button) innerContainer.lookup("#pdfButton")).setOnAction((event) -> genererPDF(reservation));
            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierReservation(reservation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerReservation(reservation));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterReservation(ActionEvent ignored) {
        currentReservation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RESERVATION);
    }

    private void modifierReservation(Reservation reservation) {
        currentReservation = reservation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RESERVATION);
    }

    private void supprimerReservation(Reservation reservation) {
        currentReservation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reservation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReservationService.getInstance().delete(reservation.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
                } else {
                    AlertUtils.makeError("Could not delete reservation");
                }
            }
        }
    }

    public void sort(ActionEvent actionEvent) {
        Reservation.compareVar = sortCB.getValue();
        Collections.sort(listReservation);
        displayData();
    }

    private void genererPDF(Reservation reservation) {
        String filename = "reservation.pdf";

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filename)));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Reservation -", font));

            document.add(new Paragraph("Date : " + reservation.getDate()));
            document.add(new Paragraph("Nombre de personnes : " + reservation.getNombreDePersonnes()));
            document.add(new Paragraph("Etablissement : " + reservation.getEtablissement().toString()));

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File(filename));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
