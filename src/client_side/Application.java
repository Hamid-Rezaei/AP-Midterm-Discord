package client_side;


public class Application {

    private static User user;

    private static void sceneHandler(){

        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> {
                    user = MenuHandler.onLoginButton();
                    if(user != null)
                        loginScene();
                }
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showStartMenu();
        }

    }


    private static void loginScene(){

        int menuChoice;
        menuChoice = MenuHandler.loginMenu();
        while (menuChoice != 4) {
            switch (menuChoice) {
                case 1 -> MenuHandler.serverMenu();
                case 2 -> friendScene();
               // case 3 -> settingScene();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.loginMenu();
        }
    }



    private static void friendScene(){

        int menuChoice;
        menuChoice = MenuHandler.friendMenu();
        while (menuChoice != 3) {
            switch (menuChoice) {
                //case 1 -> search for user;
                //case 2 -> print friend list to enter direct chat;
                default -> System.out.println("Entered Input wasn't valid.");
            }
            menuChoice = MenuHandler.friendMenu();
        }

    }


    public static void main(String[] args) {
        sceneHandler();
    }


}
