package org.frangosInfinity.core.service.module.relatorio;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFService
{
    @Autowired
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] gerarPdf(List<RelatorioVendas> relatorios)
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Paragraph titulo = new Paragraph("RELATÓRIO DE VENDAS")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(titulo);
            document.add(new Paragraph("\n"));

            if (!relatorios.isEmpty())
            {
                RelatorioVendas primeiro = relatorios.get(0);

                Paragraph periodo = new Paragraph(String.format(
                        "Início: %s | Fim: %s",
                        primeiro.getPeriodoInicio().format(DATE_FORMATTER),
                        primeiro.getPeriodoFim().format(DATE_FORMATTER)
                )).setTextAlignment(TextAlignment.CENTER);
                document.add(periodo);
                document.add(new Paragraph(" "));
            }

            Table table = new Table(UnitValue.createPointArray(new float[]{20,20,20,20,20}));
            table.setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"GERAÇÃO","INICÍO","ENCERRAMENTO","TOTAL","TICKET MÉDIO"};

            for (String header : headers)
            {
                Cell cell = new Cell().add(new Paragraph(header).setBold());
                cell.setTextAlignment(TextAlignment.CENTER);
                table.addCell(cell);
            }

            for (RelatorioVendas r : relatorios)
            {
                table.addCell(r.getDataGeracao().format(DATE_FORMATTER));
                table.addCell(r.getPeriodoInicio().format(DATE_FORMATTER));
                table.addCell(r.getPeriodoFim().format(DATE_FORMATTER));
                table.addCell(String.format("R$ %.2f", r.getTotalVendas()));
                table.addCell(String.format("R$ %.2f", r.getTicketMedio()));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.close();

            return out.toByteArray();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao gerar PDF: ", e);
        }
    }
}