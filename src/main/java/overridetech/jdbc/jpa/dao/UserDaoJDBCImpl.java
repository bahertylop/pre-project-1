package overridetech.jdbc.jpa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overridetech.jdbc.jpa.model.User;
import overridetech.jdbc.jpa.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private final Connection connection = new Util().connectJDBC();

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String createQuery = "CREATE TABLE IF NOT EXISTS account (" +
                            "    id BIGSERIAL PRIMARY KEY , " +
                            "    name VARCHAR(255) NOT NULL, " +
                            "    lastname VARCHAR(255) NOT NULL, " +
                            "    age SMALLINT NOT NULL " +
                            ")";
            statement.executeUpdate(createQuery);
        } catch (SQLException e) {
            logger.error("Ошибка при создании таблицы: {}", e.getMessage(), e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String deleteQuery = "DROP TABLE IF EXISTS account";
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            logger.error("Ошибка при удалении таблицы: {}", e.getMessage(), e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO account (name, lastname, age) VALUES (?, ?, ?)"
                )
        ) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при сохранении пользователя, name: {}, error: {}", name, e.getMessage(), e);
        }
    }

    public void removeUserById(long id) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM account WHERE id = (?)"
                )
        ) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при удалении пользователя, id: {}, error: {}", id, e.getMessage(), e);
        }

    }

    public List<User> getAllUsers() {
        String selectAllQuery = "SELECT id, name, lastname, age FROM account";
        List<User> users = new ArrayList<>();
        try (
             PreparedStatement statement = connection.prepareStatement(selectAllQuery);
             ResultSet results = statement.executeQuery();
        ) {
            while (results.next()) {
                User user = new User();
                user.setId(results.getLong("id"));
                user.setName(results.getString("name"));
                user.setLastName(results.getString("lastname"));
                user.setAge(results.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении пользователей: {}", e.getMessage(), e);
        }
        return users;
    }

    public void cleanUsersTable() {
        String cleanTableQuery = "DELETE FROM account";
        try (PreparedStatement statement = connection.prepareStatement(cleanTableQuery)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при очистке данных таблицы: {}", e.getMessage(), e);
        }
    }
}
