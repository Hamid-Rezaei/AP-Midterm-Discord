package view;

import controller.*;
import model.*;

import java.io.InputStream;

import static view.MenuHandler.sc;

public class Application {
    public static User user;
    public static AppController appController;

    private static void signUpMenu() {
        String username = MenuHandler.getUsername();
        String password = MenuHandler.getPassword();
        String email = MenuHandler.getEmail();
        System.out.print("Enter Phone number(optional): ");
        String phoneNumber = sc.nextLine();
        if(phoneNumber == null){
            phoneNumber = "";
        }
        InputStream img = MenuHandler.getAvatar();
        if (appController.signUp(username, password, email, phoneNumber, img)) {
            System.out.println("SignUp successfully");
        } else {
            System.out.println("SignUp error");
        }
    }

    public static void main(String[] args) {
        appController = new AppController();
        appController.initializeNetwork();
        int choice = MenuHandler.showStartMenu();
        switch (choice) {
            case 1 -> signUpMenu();
        }


    }

}
