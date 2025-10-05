package com.example.base.ui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCode{

    public static void generateQRCodeForWebsite(String url, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    public static void main(String[] args) {
        try {
            String website = "http://localhost:8080/";
            generateQRCodeForWebsite(website, 300, 300, "qrcode_website.png");
            System.out.println("✅ QR Code criado com sucesso: qrcode_website.png");
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar QR Code: " + e.getMessage());
        }
    }
}
