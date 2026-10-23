package service;

public class AuthenticationService {
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "password123".equals(password);
    }
}