package controllers.front.post;


import service.etaAct.*;
import modeles.etaAct.Post;
import controllers.front.MainWindowController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.etatAct.Constants;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Post currentPost;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;


    List<Post> listPost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listPost = PostService.getInstance().getAll();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listPost);

        if (!listPost.isEmpty()) {
            for (Post post : listPost) {
                if (searchText.isEmpty() || post.getTitre().toLowerCase().contains(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makePostModel(post));
                }
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makePostModel(
            Post post
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_POST)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + post.getTitre());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + post.getDescription());
            ((Text) innerContainer.lookup("#localisationText")).setText("Localisation : " + post.getLocalisation());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + post.getDate());


            Path selectedImagePath = FileSystems.getDefault().getPath(post.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }


            try {
                String data = post.allAttrToString();
                String path = "./qr_code.jpg";
                BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
                MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));

                Path qrPath = FileSystems.getDefault().getPath(path);
                if (qrPath.toFile().exists()) {
                    ((ImageView) innerContainer.lookup("#qrImage")).setImage(new Image(qrPath.toUri().toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((Button) innerContainer.lookup("#genererPDF")).setOnAction((event) -> genererPDF(post));
            ((Button) innerContainer.lookup("#viewCommentButton")).setOnAction((event) -> showComments(post));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void showComments(Post post) {
        currentPost = post;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_COMMENTAIRE);
    }

    @FXML
    public void search(KeyEvent actionEvent) {
        displayData(searchTF.getText());
    }

    private void genererPDF(Post post) {
        String filename = "post.pdf";

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filename)));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Post -"));

            try {
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(post.getImage());
                image.scaleAbsoluteWidth(300);
                image.scaleAbsoluteHeight(300);
                image.isScaleToFitHeight();
                document.add(image);
            } catch (FileNotFoundException e) {
                System.out.println("Image not found");
            }

            document.add(new Paragraph("Titre : " + post.getTitre()));
            document.add(new Paragraph("Description : " + post.getDescription()));
            document.add(new Paragraph("Localisation : " + post.getLocalisation()));
            document.add(new Paragraph("Date : " + post.getDate()));

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File(filename));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
