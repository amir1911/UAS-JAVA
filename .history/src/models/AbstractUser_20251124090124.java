package models;

public abstract class AbstractUser {
    private String username;
    private String password;

    public AbstractUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Encapsulation
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    // Polymorphism (akan dioverride di subclass)
    public abstract String getRole();
}
