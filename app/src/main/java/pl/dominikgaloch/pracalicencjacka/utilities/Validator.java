package pl.dominikgaloch.pracalicencjacka.utilities;

public class Validator {
    public static boolean validateName(String name)
    {
        if(name.matches("[A-Za-z0-9]{3,})"))
            return true;
        return false;
    }
    public static boolean validateDescription(String description) {
        return description.matches("(^.{5,1000}$)|(^$)");
    }

    public static boolean validateCoord(String coordinate) {
        return coordinate.matches("(^-*\\d+.{1}\\d+$)|(^-*\\d+$)");
    }
}
