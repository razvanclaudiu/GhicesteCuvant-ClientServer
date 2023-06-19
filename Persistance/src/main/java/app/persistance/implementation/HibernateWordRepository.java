package app.persistance.implementation;

import app.model.Word;
import app.persistance.WordRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;

import java.util.List;

public class HibernateWordRepository implements WordRepository {
    @Override
    public Word get(Long aLong) {
        return null;
    }

    @Override
    public List<Word> getAll() {
        return null;
    }

    @Override
    public Word save(Word entity) throws RepositoryException{
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
        catch(Exception ex){
            System.out.println("Error while saving word: " + ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public Word remove(Long aLong) {
        return null;
    }

    @Override
    public Word update(Word entity) {
        return null;
    }
}
