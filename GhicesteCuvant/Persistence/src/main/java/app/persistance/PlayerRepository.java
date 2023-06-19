package app.persistance;

import app.model.User;
import app.persistance.utils.RepositoryException;

public interface PlayerRepository extends CRUDRepository<Long, User> {
    User checkCredentials(String username, String password) throws RepositoryException;
}
