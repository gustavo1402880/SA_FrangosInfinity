package org.frangosInfinity.infrastructure.console.module.relatorio;

import java.awt.*;
import java.io.FileOutputStream;

public class RelatorioView
{
    public static void salvar(byte[] pdf) {

        FileDialog fileDialog = new FileDialog((Frame) null, "Salvar PDF", FileDialog.SAVE);

        fileDialog.setFile("relatorio.pdf");
        fileDialog.setVisible(true);

        String diretorio = fileDialog.getDirectory();
        String arquivo = fileDialog.getFile();

        if (diretorio != null && arquivo != null) {

            if (!arquivo.toLowerCase().endsWith(".pdf")) {
                arquivo += ".pdf";
            }

            try (FileOutputStream fos = new FileOutputStream(diretorio + arquivo)) {
                fos.write(pdf);
                System.out.println("PDF salvo em: " + diretorio + arquivo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Usuário cancelou.");
        }
    }
}