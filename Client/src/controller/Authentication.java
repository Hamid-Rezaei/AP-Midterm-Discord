package controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.Handler.matchedInput;

/**
 * The type Handler.
 */
abstract class Handler {
    /**
     * The Next handler.
     */
    protected Handler nextHandler;

    /**
     * Sets next handler.
     *
     * @param nextHandler the next handler
     */
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }


    /**
     * Matched input boolean.
     *
     * @param regex the regex
     * @param input the input
     * @return the boolean
     */
    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    /**
     * Handle string.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @return the string
     */
    public abstract String handle(String username, String password, String email, String phoneNum);
}


/**
 * The type Username handler.
 */
class UsernameHandler extends Handler {

    @Override
    public String handle(String username, String password, String email,String phoneNum) {
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        if (!matchedInput(usernameRegex, username)) {
            return "Error - Username length must be at least 6 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email,phoneNum);
            return "Success";
        }
    }
}

/**
 * The type Password handler.
 */
class PasswordHandler extends Handler {

    @Override
    public String handle(String username, String password, String email,String phoneNum) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        if (!matchedInput(passwordRegex, password)) {
            return "Error - Password length must be at least 8 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email,phoneNum);
            return "Success";
        }
    }
}


/**
 * The type Email handler.
 */
class EmailHandler extends Handler {

    @Override
    public String handle(String username, String password, String email,String phoneNum) {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        if (!matchedInput(emailRegex, email)) {
            return "Error - Provided email wasn't valid.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email,phoneNum);
            return "Success";
        }
    }
}

class PhoneHandler extends Handler {

    @Override
    public String handle(String username, String password, String email,String phoneNum) {
        String phoneRegex = "^\\d{11}$";
        if (!matchedInput(phoneRegex, phoneNum)) {
            return "Error - Provided phone number wasn't valid.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email,phoneNum);
            return "Success";
        }
    }
}

/**
 * The type Authentication.
 */
public class Authentication {
    /**
     * Check validation of info string.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @return the string
     */
    public static String checkValidationOfInfo(String username, String password, String email, String phoneNum) {
        UsernameHandler usernameHandler = new UsernameHandler();
        PasswordHandler passwordHandler = new PasswordHandler();
        EmailHandler emailHandler = new EmailHandler();
        PhoneHandler phoneHandler = new PhoneHandler();
        usernameHandler.setNextHandler(passwordHandler);
        passwordHandler.setNextHandler(emailHandler);
        emailHandler.setNextHandler(phoneHandler);
        return usernameHandler.handle(username, password, email, phoneNum);
    }

    /**
     * Check valid pass boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public static boolean checkValidPass(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        return matchedInput(passwordRegex, password);
    }
}
