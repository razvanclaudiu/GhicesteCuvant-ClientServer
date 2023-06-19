package app.persistance.implementation;

import app.model.User;
import app.persistance.PlayerRepository;
import app.persistance.utils.JdbcUtils;
import app.persistance.utils.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DefaultPlayerRepository implements PlayerRepository {

    private final JdbcUtils dbUtils;

    public DefaultPlayerRepository(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

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
        Connection connection = dbUtils.getConnection();

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    User user = new User(id, username, password);
                    return user;
                }
            }

        }
        catch (SQLException ex){
            throw new RepositoryException("Error DB " + ex);
        }
        throw new RepositoryException("Authentication failed.");
    }
}


