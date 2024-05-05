package service.reclamation;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGeneratorService {

    public void generatePDF(String text, String filePath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Ajouter le titre au milieu du document avec une couleur bleue
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
        Paragraph title = new Paragraph("Titre du document", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Ajouter de l'espace entre le titre et l'image
        document.add(new Paragraph(" ")); // Ajoutez un espace vide

        // Ajouter une image au document
        Image img = Image.getInstance("assets/images/back.jpg");


        img.setAlignment(Element.ALIGN_CENTER);
        document.add(img);

        // Ajouter de l'espace entre l'image et le texte
        document.add(new Paragraph(" ")); // Ajoutez un espace vide

        // Ajouter du texte au document avec une couleur spécifique
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
        Paragraph paragraph = new Paragraph(text, textFont);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);

        document.close();
    }
    public static void main(String[] args) {
        PDFGeneratorService pdfGeneratorService = new PDFGeneratorService();
        try {
            pdfGeneratorService.generatePDF("Contenu du PDF", "assets/images/");
            System.out.println("PDF généré avec succès !");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération du PDF.");
        }
    }}
