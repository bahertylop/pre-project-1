package overridetech.jdbc.jpa;

import overridetech.jdbc.jpa.dao.UserDao;
import overridetech.jdbc.jpa.dao.UserDaoHibernateImpl;
import overridetech.jdbc.jpa.dao.UserDaoJDBCImpl;
import overridetech.jdbc.jpa.model.User;
import overridetech.jdbc.jpa.service.UserService;
import overridetech.jdbc.jpa.service.UserServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserDao jdbcDao = new UserDaoJDBCImpl();
        UserDao hibernateDao = new UserDaoHibernateImpl();
        UserService userService = new UserServiceImpl(hibernateDao);

        userService.createUsersTable();

        for (int i = 0; i < 4; i++) {
            userService.saveUser("name" + i, "last_name" + i, (byte) (i + 1));
            System.out.println("\tUser с именем – " + ("name" + i) +" добавлен в базу данных");
        }

        System.out.println("Все пользователи из таблицы:");
        List<User> users = userService.getAllUsers();
        users.forEach((x) -> System.out.println("\t" + x));

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
