package pl.dominikgaloch.pracalicencjacka.utilities;

public class Validator {
    public static boolean validateName(String name) {
        return name.matches("^[A-Za-z0-9ĄĆĘŁŃÓŚŹŻąćęłńóśźż ]{3,30}$");
    }

    public static boolean validateDescription(String description) {
        return description.matches("(^.{5,1000}$)|(^$)");
    }

    public static boolean validateCoord(String coordinate) {
        return coordinate.matches("(^-{0,1}\\d+\\.{1}\\d+$)|(^-{0,1}\\d+$)");
    }
}
