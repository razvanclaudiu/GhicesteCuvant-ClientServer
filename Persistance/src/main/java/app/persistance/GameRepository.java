package app.persistance;

import app.model.Game;
import app.persistance.utils.RepositoryException;

import java.util.List;

public interface GameRepository extends CRUDRepository<Long, Game>{
    List<Game> getAllWithId(Long id) throws RepositoryException;

    Long getMaxGameID() throws RepositoryException;
}
