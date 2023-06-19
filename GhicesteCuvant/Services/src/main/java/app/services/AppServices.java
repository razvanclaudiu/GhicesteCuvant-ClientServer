package app.services;

import app.model.User;

public interface AppServices {
    User login(String username, String password, AppObserver clientObserver) throws AppException;

    void logout(User user) throws AppException;
}
