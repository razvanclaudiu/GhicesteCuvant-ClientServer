package app.persistance.implementation;

import app.model.User;
import app.persistance.PlayerRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;

import java.util.List;

public class DefaultPlayerRepository implements PlayerRepository {

    @Override
    public User get(Long aLong) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public User remove(Long aLong) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public User checkCredentials(String username, String password) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            User user = session
                    .createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            session.getTransaction().commit();
            return user;
        }
        catch(Exception ex){
            System.out.println("Error while checking credentials: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }
}


