
        package com.furnadelampiao;

import com.furnadelampiao.repository.*;
import com.furnadelampiao.service.*;
import com.furnadelampiao.view.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MenuView menuView = new MenuView(scanner);

        menuView.menu();

    }
}
