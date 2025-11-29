package services;

import java.util.ArrayList;
import models.AbstractUser;
import models.Admin;
import models.User;

public class AuthService {

    private ArrayList<AbstractUser> userList = new ArrayList<>();

    public AuthService() {
        // default admin
        userList.add(new Admin("admin", "12345"));
    }

    public void register(String username, String password) {
        userList.add(new User(username, password));
    }

    public AbstractUser login(String username, String password) {
        for (AbstractUser u : userList) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password)) {
                return u; // return user jika cocok
            }
        }
        return null;
    }
}
