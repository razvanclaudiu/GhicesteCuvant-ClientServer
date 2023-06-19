package app.persistance.implementation;

import app.model.Game;
import app.persistance.GameRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateGameRepository implements GameRepository {


    @Override
    public Game get(Long aLong) {
        return null;
    }

    @Override
    public List<Game> getAll() {
        return null;
    }

    @Override
    public Game save(Game entity) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
        catch(Exception ex){
            System.out.println("Error while saving game: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public Game remove(Long aLong) {
        return null;
    }

    @Override
    public Game update(Game entity) throws RepositoryException{
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return entity;
        }
        catch(Exception ex){
            System.out.println("Error while updating game: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Game> getAllWithId(Long id) throws RepositoryException{
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<Game> games = session.createQuery("from Game where game_id = :id", Game.class)
                    .setParameter("id", id)
                    .list();
            session.getTransaction().commit();
            return games;
        }
        catch(Exception ex){
            System.out.println("Error while getting games with id: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public Long getMaxGameID() throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            Long maxId = session.createQuery("select max(game_id) from Game", Long.class).uniqueResult();
            session.getTransaction().commit();
            return maxId+1;
        }
        catch(Exception ex){
            System.out.println("Error while getting max game id: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }
}
