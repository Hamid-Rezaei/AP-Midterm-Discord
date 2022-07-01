package controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.Handler.matchedInput;

abstract class Handler {
    protected Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }


    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public abstract String handle(String username, String password, String email);
}


class UsernameHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        if (!matchedInput(usernameRegex, username)) {
            return "Error - Username length must be at least 6 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}

class PasswordHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        if (!matchedInput(passwordRegex, password)) {
            return "Error - Password length must be at least 8 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}


class EmailHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        if (!matchedInput(emailRegex, email)) {
            return "Error - Provided email wasn't valid.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}


public class Authentication {
    public static String checkValidationOfInfo(String username, String password, String email) {
        UsernameHandler usernameHandler = new UsernameHandler();
        PasswordHandler passwordHandler = new PasswordHandler();
        EmailHandler emailHandler = new EmailHandler();
        usernameHandler.setNextHandler(passwordHandler);
        passwordHandler.setNextHandler(emailHandler);

        return usernameHandler.handle(username, password, email);
    }

    public static boolean checkValidPass(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        return matchedInput(passwordRegex, password);
    }
}
