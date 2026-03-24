package org.frangosInfinity.core.service.module.relatorio;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PDFService {

    public byte[] gerarPdf(List<RelatorioVendas> relatorios) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            document.add(new Paragraph("RELATÓRIO DE VENDAS"));

            // Tabela (4 colunas)
            Table table = new Table(4);

            table.addHeaderCell("Data");
            table.addHeaderCell("Pedidos");
            table.addHeaderCell("Vendas");
            table.addHeaderCell("Ticket Médio");

            for (RelatorioVendas r : relatorios) {
                table.addCell(r.getDataGeracao().toString());
                table.addCell(String.valueOf(r.getTotalPedidos()));
                table.addCell(String.valueOf(r.getTotalVendas()));
                table.addCell(String.valueOf(r.getTicketMedio()));
            }

            document.add(table);

            document.close();

            return out.toByteArray();
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}