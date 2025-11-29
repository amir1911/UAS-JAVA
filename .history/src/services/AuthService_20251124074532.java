package services;

import models.AbstractUser;

public class AuthService {

    // Asosiasi ke AbstractUser
    public boolean login(AbstractUser user, String username, String password) {
        return user.getUsername().equals(username) &&
               user.getPassword().equals(password);
    }
}
