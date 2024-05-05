package controllers.evenement;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static byte[] generateQRCode(String baseUrl, int eventId) {
        int width = 300;
        int height = 300;
        String format = "png";

        String eventDetailsUrl = baseUrl + "/eventdetails/" + eventId;


        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        // Create QR code writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // Encode QR code content into a bit matrix
            BitMatrix bitMatrix = qrCodeWriter.encode(eventDetailsUrl, BarcodeFormat.QR_CODE, width, height, hints);

            // Convert the bit matrix to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null; // Return null in case of failure
        }
    }


}
