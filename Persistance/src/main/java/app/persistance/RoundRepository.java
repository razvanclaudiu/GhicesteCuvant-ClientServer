package app.persistance;

import app.model.Round;
import app.persistance.utils.RepositoryException;

public interface RoundRepository extends CRUDRepository<Long, Round>{
    Long getMaxRoundID() throws RepositoryException;
}
