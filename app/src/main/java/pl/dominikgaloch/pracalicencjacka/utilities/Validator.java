package pl.dominikgaloch.pracalicencjacka.utilities;

public class Validator {
    public boolean validName(String name)
    {
        if(name.matches("[A-Za-z0-9]{3,})"))
            return true;
        return false;
    }
    public boolean validDescription(String description) {
        if(description.matches("[A-Za-z0-9]*"))
            return true;
        return false;
    }
}
