package controllers.reclamation;


import service.reclamation.*;
import modeles.reclamation.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Font;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationController implements Initializable {

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
    private TilePane myTilePane;

    @FXML
    private Label itemselcted;
    @FXML
    void ajouter(ActionEvent event) {
        if (isValidInput()) {
            ReclamationService rs = new ReclamationService();

            rs.ajouter(new Reclamation(
                    1,
                    objet.getText(),
                    text.getText(),
                    this.sta
            ));

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

        load();  // Load data and set up other initializations
    }

    @FXML
    void search(ActionEvent event) {
        String searchQuery = searchField.getText().toLowerCase().trim(); // Get the search query from the TextField
        if (!searchQuery.isEmpty()) {
            ObservableList<Node> matchedNodes = FXCollections.observableArrayList(); // Store matched cards

            for (Node node : myTilePane.getChildren()) {
                VBox card = (VBox) node;
                Label labelObjet = (Label) card.getChildren().get(0); // Assuming the first child is the label for object

                if (labelObjet.getText().toLowerCase().contains(searchQuery)) {
                    matchedNodes.add(card); // Add the card to the list of matched nodes
                }
            }

            myTilePane.getChildren().clear(); // Clear existing content
            myTilePane.getChildren().addAll(matchedNodes); // Add matched cards to the TilePane
        } else {
            // If search query is empty, reload all reclamations
            load();
        }
    }

    private VBox createCard(Reclamation reclamation) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card-style");

        // Customize your card layout here, e.g., add labels, buttons, etc.
        Label labelObjet = new Label("Objet: " + reclamation.getObjet());
        labelObjet.getStyleClass().add("card-title");

        Label labelText = new Label("Text: " + reclamation.getText());
        labelText.getStyleClass().add("card-text");

        card.getChildren().addAll(labelObjet, labelText);
        return card;
    }





    void load() {
        ReclamationService rs = new ReclamationService(); // Assuming you have a service class
        List<Reclamation> reclamations = rs.afficher(); // Call the method to retrieve reclamations

        myTilePane.getChildren().clear(); // Clear existing content
        for (Reclamation reclamation : reclamations) {
            VBox card = createCard(reclamation); // Assuming createCard creates a VBox card for each publication
            myTilePane.getChildren().add(card);
        }
    }

    @FXML
    void makePdf(ActionEvent event) {
        Reclamation selectedLivraison = recl.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            Document document = new Document();
            try {
                String filePath = "Reclamation.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                document.add(new Paragraph("Reclamation", FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD, BaseColor.BLUE)));
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("ID Reclamation: " + selectedLivraison.getId()));
                document.add(new Paragraph("Text: " + selectedLivraison.getText()));
                document.add(new Paragraph("Objet: " + selectedLivraison.getObjet()));

                document.close();

                if (itemselcted != null) {
                    itemselcted.setText("PDF généré avec succès !");
                }
                Desktop.getDesktop().open(new File(filePath));
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                if (itemselcted != null) {
                    itemselcted.setText("Erreur lors de la génération du PDF: " + e.getMessage());
                }
            }
        } else {
            if (itemselcted != null) {
                itemselcted.setText("Aucune réclamation sélectionnée !");
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
    @FXML
    void navigateToResponse(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/reponse.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Trietext() {
        ReclamationService l = new ReclamationService();
        Reclamation ld = new Reclamation();
        List<Reclamation> list = l.triText();
        recl.getItems().setAll(list);
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
