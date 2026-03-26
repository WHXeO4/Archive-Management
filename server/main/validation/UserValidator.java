package main.validation;



public class UserValidator {
    public static boolean usernameValidator(String name) {
        return name.length() >= 5 && name.length() <= 16;
    }

    public static boolean passwordValidator(String password) {
        if (password.length()<6 || password.length()>16) {
            return false;
        }

        for(Character c : password.toCharArray()) {
            if (!Character.isDigit(c)
                && !Character.isAlphabetic(c)){
                return false;
            }
        }

        return true;
    }
}
