package models;

public class Admin extends AbstractUser {

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Administrator";
    }
}
