package controllers.reclamation;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.awt.Font;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationBackController implements Initializable {

    @FXML
    private TextField objet;

    private int sta;

    @FXML
    private ListView<Reclamation> recl;

    @FXML
    private TextField text;

    @FXML
    private ComboBox<String> etat;
    @FXML
    private TextField searchField;
    @FXML
    private Label itemselcted;
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
    private ComboBox<String> triChoiceBox;

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
