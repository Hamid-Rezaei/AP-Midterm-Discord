package client_side;
<<<<<<< HEAD
=======
>>>>>>> af86eab3cd9f7e4b03048a928bea40cfefee744d


public class Application {

    private static User user;

    public static void main(String[] args) {
        int choice = MenuHandler.showStartMenu();
        while (choice != 3) {
            switch (choice) {
                case 1 -> MenuHandler.onSignUpButton();
                case 2 -> user = MenuHandler.onLoginButton();
                default -> System.out.println("Entered Input wasn't valid.");
            }
            choice = MenuHandler.showStartMenu();
        }

    }
}
