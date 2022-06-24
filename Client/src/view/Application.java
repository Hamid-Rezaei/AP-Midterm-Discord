package view;

import controller.*;
import model.*;

import java.io.InputStream;

import static view.MenuHandler.showLoginMenu;
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
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        InputStream img = MenuHandler.getAvatar();
        String authenticationStatus = Authentication.checkValidationOfInfo(username, password, email);
        if (authenticationStatus.equals("Success")) {
            String signUpResult = appController.signUp(username, password, email, phoneNumber, img);
            if (signUpResult.equals("Success")) {
                System.out.println("SignUp successfully");
            } else {
                System.out.println(signUpResult);
                signUpMenu();
            }
        } else {
            System.out.println(authenticationStatus);
            signUpMenu();
        }

    }


    private static void loginMenuHandler(){
        String username = MenuHandler.getUsername();
        String password = MenuHandler.getPassword();
        user = appController.login(username, password);
        if (user == null){
            //loginMenuHandler();
            System.out.println("user is null");
        }else{
            System.out.println("Login Successfully.");
        }
    }

    private static void runApp() {
        runLoop:
        while (true) {
            int choice = MenuHandler.showStartMenu();
            switch (choice) {
                case 1 -> signUpMenu();
                case 2 -> loginMenuHandler();
                case 3 -> {
                    break runLoop;
                }
                default -> choice = MenuHandler.showStartMenu();
            }

        }
    }

    public static void main(String[] args) {
        appController = new AppController();
        runApp();
    }

}
