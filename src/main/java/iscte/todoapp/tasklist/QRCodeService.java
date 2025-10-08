package iscte.todoapp.tasklist;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.flow.server.StreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class QRCodeService {

    public String generateBulletPointQRCodeBase64(List<String> items, int width, int height) {
        try {
            // Build text content with bullet points
            StringBuilder sb = new StringBuilder();
            for (String item : items) {
                sb.append("• ").append(item).append("\n");
            }
            String qrContent = sb.toString().trim();

            // Generate QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

            // Convert to byte[]
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Convert to Base64
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

}
