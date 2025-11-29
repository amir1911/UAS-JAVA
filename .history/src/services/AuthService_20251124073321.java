package services;

import models.User;

public class AuthService {

    public boolean login(User user, String username, String password) {
        return user.getUsername().equals(username)
            && user.getPassword().equals(password);
    }
}
