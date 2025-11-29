package models;

public class User extends AbstractUser {

    public User(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "User ";
    }
}
