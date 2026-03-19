package org.frangosInfinity.infrastructure.util;

import java.util.Scanner;

public class Front
{
    // Cores padrão
    public static final String AZUL_ACINZENTADO = "\u001B[38;2;10;20;80m"; // RAL 5009
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Cores brilhantes
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // Fundos (backgrounds)
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // Fundos brilhantes
    public static final String BG_BRIGHT_BLACK = "\u001B[100m";
    public static final String BG_BRIGHT_RED = "\u001B[101m";
    public static final String BG_BRIGHT_GREEN = "\u001B[102m";
    public static final String BG_BRIGHT_YELLOW = "\u001B[103m";
    public static final String BG_BRIGHT_BLUE = "\u001B[104m";
    public static final String BG_BRIGHT_PURPLE = "\u001B[105m";
    public static final String BG_BRIGHT_CYAN = "\u001B[106m";
    public static final String BG_BRIGHT_WHITE = "\u001B[107m";
    // Estilos
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String REVERSED = "\u001B[7m";
    public static final String ITALIC = "\u001B[3m";

    // TONS UTILIZADOS NO MENU
    public static final String OCEAN_BLUE = "\u001B[38;5;25m";          //  UTILIZADO NA LOGO WEG
    public static final String ORANGE_DARK = "\u001B[38;2;210;110;0m";  //  UTILIZADO NOS MENUS - DETALHES
    public static final String AQUA_BLUE = "\u001B[38;5;31m";           //  UTILIZADO NA BASE DO MENU
    public static final String DARK_CYAN = "\u001B[38;5;30m";           //  UTILIZADO NO PROCESSAMENTO DE DADOS
    public static final String TEAL_LIGHT = "\u001B[38;2;63;166;176m";  //  UTILIZADO EM UMA PARTE MUITO ESPECÍFICA(SOMENTE PARA UM DETALHE DE VALOR DE ORDEM DE SERVIÇO)
    //NAS CONFIGURAÇÕES SETTINGS -> EDITOR -> COLOR SCHEME -> CONSOLE COLORS -> USER INPUT -> FOREGROUND -> MUDA O HEX PARA: 3FA6B0

    static Scanner ler = new Scanner(System.in);


    public static void Delay(int ms) {
        try{Thread.sleep(ms);}catch(InterruptedException e){}
    }


    // ------ FAZ INPUT DE DOUBLE E RETORNA ------ //
    public static double lDouble() {

        try {
            double num = ler.nextDouble();
            ler.nextLine();

            return num;
        }catch (Exception e) {

            ler.nextLine(); // Esvazia o buffer

            throw e;
        }
    }

    // ------ FAZ INPUT DE STRING E RETORNA ------ //
    public static String lString() {

        try {
            String t = ler.nextLine();

            return t;
        }catch (Exception e){
            ler.nextLine();

            throw e;
        }
    }

    // ------ FAZ INPUT DE INTEIRO E RETORNA ------ //
    public static int lInteiro() {
        try{
            int num = ler.nextInt();
            ler.nextLine();

            return num;
        } catch(Exception e) {
            ler.nextLine();

            throw e;
        }
    }

    // ------ PULA MUITAS LINHAS DO TERMINAL ------ //
    public static void limpaTerminal() {
        for(int i = 0; i < 50; i ++) {
            System.out.println();
        }
    }

    // ------- RECEBE MENSAGEM DE ERRO E EXIBE ------- //
    public static void mensagemErro(String mensagem) {
        Front.limpaTerminal();
        System.err.println(mensagem);
        Front.Delay(2000);
        Front.limpaTerminal();
    }

    // ------- RECEBE MENSAGEM DE SUCESSO E EXIBE ------- //
    public static void mensagemSucesso(String mensagem) {
        Front.limpaTerminal();
        System.out.println(BLUE + mensagem + RESET);
        Front.Delay(2000);
        Front.limpaTerminal();
    }

    // ------- MENU UTILIZADO PARA CASES DEFAULT ------- //
    public static void menuDefault() {
        Front.limpaTerminal();
        System.err.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.err.println("┃                Valor digitado incorreto!               ┃");
        System.err.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        Front.Delay(2000);
        Front.limpaTerminal();
    }

    public static class ProcessamentoDeDados {
        public static void main() {
            System.out.println(Front.ITALIC+Front.DARK_CYAN+"┃  PROCESSANDO DADOS..."+Front.RESET+Front.AQUA_BLUE);

            int total = 100;

            for (int i = 0; i <= total; i++) {
                mostrarBarraProgresso(i, total);

                try {
                    // Simula tempo de trabalho
                    Thread.sleep(25);
                } catch (InterruptedException e) { }
            }
        }

        public static void mostrarBarraProgresso(int atual, int total) {
            int tamanhoBarra = 50; // Tamanho visual da barra em caracteres
            int porcentagem = (int) ((atual * 100.0f) / total);
            int progressoChars = (int) ((atual * 100.0f) / total * tamanhoBarra / 100);

            StringBuilder barra = new StringBuilder();
            barra.append(DARK_CYAN+"[");

            for (int i = 0; i < tamanhoBarra; i++) {
                if (i < progressoChars) {
                    barra.append(DARK_CYAN+"=");
                } else if (i == progressoChars) {
                    barra.append(DARK_CYAN+">");
                } else {
                    barra.append(DARK_CYAN+" ");
                }
            }

            barra.append(DARK_CYAN+"] " + porcentagem + "%");

            // \r sobrescreve a linha atual
            System.out.print(DARK_CYAN+"\r" + barra.toString());
        }
    }

}
