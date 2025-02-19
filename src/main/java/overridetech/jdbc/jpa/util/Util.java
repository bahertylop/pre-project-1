package overridetech.jdbc.jpa.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overridetech.jdbc.jpa.dao.UserDaoHibernateImpl;
import overridetech.jdbc.jpa.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/pre_project";
    static final String USER = "postgres";
    static final String PASS = "agroycg592:ggg";

    public Connection connectJDBC() {
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            logger.error("Ошибка при подключении к базе данных: {}", e.getMessage(), e);
            throw new RuntimeException();
        }

        logger.info("Успешное подключение к базе данных");
        return connection;
    }

    public SessionFactory connectHibernate() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata();

        SessionFactory sessionFactory = metadata.buildSessionFactory();

        if (sessionFactory == null) {
            logger.error("Не удалось подключиться к базе данных");
        } else {
            logger.info("Успешное подключение к базе данных");
        }
        return sessionFactory;
    }
}
