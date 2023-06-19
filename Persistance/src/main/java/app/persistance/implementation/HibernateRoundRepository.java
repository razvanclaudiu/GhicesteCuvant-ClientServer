package app.persistance.implementation;

import app.model.Round;
import app.persistance.RoundRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;

import java.util.List;

public class HibernateRoundRepository implements RoundRepository {
    @Override
    public Round get(Long aLong) {
        return null;
    }

    @Override
    public List<Round> getAll() {
        return null;
    }

    @Override
    public Round save(Round entity) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
        catch(Exception ex){
            System.out.println("Error while saving round: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public Round remove(Long aLong) {
        return null;
    }

    @Override
    public Round update(Round entity) throws RepositoryException {
        return null;
    }

    @Override
    public Long getMaxRoundID() throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            Long maxID = (Long) session.createQuery("select max(id) from Round").uniqueResult();
            session.getTransaction().commit();
            return maxID;
        }
        catch(Exception ex){
            System.out.println("Error while getting max round id: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }
}
