package com.furnadelampiao.view;

import java.util.Scanner;

public class MenuView {

    private final Scanner scanner;

    public MenuView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        System.out.println();
        System.out.println("================================");
        System.out.println("       FURNA DE LAMPIÃO");
        System.out.println("================================");
        System.out.println("1 - Gerenciar cavernas");
        System.out.println("2 - Gerenciar setores");
        System.out.println("3 - Gerenciar pessoas");
        System.out.println("4 - Gerenciar expedições");
        System.out.println("0 - Sair");
        System.out.println("================================");
        System.out.print("Escolha: ");
    }

    public int lerOpcao() {
        return Integer.parseInt(scanner.nextLine());
    }
}