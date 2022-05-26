package client_side;


public class Application {

    private static User user;

    public static void main(String[] args) {
        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> {
                    user = MenuHandler.onLoginButton();
                    user.sceneHandler();
                }
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showStartMenu();
        }

    }
}
