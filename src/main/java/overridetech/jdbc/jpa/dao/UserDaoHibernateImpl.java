package overridetech.jdbc.jpa.dao;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overridetech.jdbc.jpa.model.User;
import overridetech.jdbc.jpa.util.Util;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    private final SessionFactory sessionFactory = new Util().connectHibernate();

    @Override
    public void createUsersTable() {
        String createQuery = "CREATE TABLE IF NOT EXISTS account (" +
                "    id BIGSERIAL PRIMARY KEY , " +
                "    name VARCHAR(255) NOT NULL, " +
                "    lastname VARCHAR(255) NOT NULL, " +
                "    age SMALLINT NOT NULL " +
                ")";
        executeSql(createQuery);
    }

    @Override
    public void dropUsersTable() {
        String deleteQuery = "DROP TABLE IF EXISTS account";
        executeSql(deleteQuery);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession();) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка при сохранении пользователя, name: {}, error: {}", name, e.getMessage(), e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession();) {
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка при удалении пользователя, id: {}, error: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession();) {
            users = session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            logger.error("Ошибка при получении пользователей: {}", e.getMessage(), e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession();) {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM User").executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка при очистке данных из таблицы: {}", e.getMessage(), e);
        }
    }

    private void executeSql(String sql) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка при выполнении нативного запроса, запрос: {}, исключение: {}", sql, e.getMessage(), e);
        }
    }
}
