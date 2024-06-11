package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        //Закомментировать ненужное
        //Работа чисто с JDBC
        //UserDao userDao = new UserDaoJDBCImpl();
        //Работа с использованием хибернейт
        UserDao userDao = new UserDaoHibernateImpl();
        UserService userService = new UserServiceImpl(userDao);
        userService.createUsersTable();
        for (int i = 0; i < 4; i++) {
            userService.saveUser("name" + i, "lastName" + i, (byte) (18 + i));
        }
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
