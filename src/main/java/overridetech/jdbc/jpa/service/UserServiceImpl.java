package overridetech.jdbc.jpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overridetech.jdbc.jpa.dao.UserDao;
import overridetech.jdbc.jpa.dao.UserDaoHibernateImpl;
import overridetech.jdbc.jpa.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUsersTable() {
        userDao.createUsersTable();
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        if (name == null || lastName == null || age <= 0) {
            logger.error("Переданы некорректные параметры; name: {}, lastName: {}, age: {}", name, lastName, age);
            return;
        }

        userDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        if (id <= 0) {
            logger.error("Переданы некорректные параметры; id: {}", id);
            return;
        }

        userDao.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void cleanUsersTable() {
        userDao.cleanUsersTable();
    }
}
