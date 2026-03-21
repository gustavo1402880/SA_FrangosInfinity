package org.frangosInfinity.infrastructure.console.module.main;

import java.util.Scanner;

public class MenuPrincipal
{
    static Scanner scanner = new Scanner(System.in);
    public static void menuPrincipal()
    {
        while (true) {

            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║             MENU PRINCIPAL             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1 - Login");
            System.out.println("0 - Sair");

            int op = scanner.nextInt();

            switch (op) {
                case 1 -> MenuLogin.login();
                case 0 -> EncerarSistema.desligar();
            }
        }
    }
}
