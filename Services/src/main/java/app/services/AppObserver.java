package app.services;

import app.model.User;

public interface AppObserver {

    void notifyLogin(Integer noOfUsers) throws AppException;

    void notifyLogout(Integer noOfUsers) throws AppException;
}
