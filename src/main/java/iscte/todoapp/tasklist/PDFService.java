package iscte.todoapp.tasklist;


import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.*;
//import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PDFService {

    public ByteArrayInputStream gerarRelatorio(String nomeUtilizador, List<String> tarefas) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Cabeçalho
            document.add(new Paragraph("Relatório de Tarefas")
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(10));

            document.add(new Paragraph("Utilizador: " + nomeUtilizador));
            document.add(new Paragraph("Data de exportação: " + LocalDate.now()));
            document.add(new Paragraph("\n"));

            // Tabela de tarefas
            //UnitValue UnitValue = null;
            Table tabela = new Table(UnitValue.createPercentArray(new float[]{100}))
                    .useAllAvailableWidth();

            tabela.addHeaderCell(new Cell().add(new Paragraph("Tarefa").setBold()));
            //tabela.addHeaderCell(new Cell().add(new Paragraph("Estado").setBold()));

            for (String tarefa : tarefas) {
                tabela.addCell(new Cell().add(new Paragraph(tarefa)));
               // tabela.addCell(new Cell().add(new Paragraph("Concluída")));
            }

            document.add(tabela);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
