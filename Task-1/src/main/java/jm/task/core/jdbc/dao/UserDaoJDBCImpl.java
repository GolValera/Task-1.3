package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try {
            //Чтобы создать таблицу, необходимо прописать скрипт ниже
            //Создать таблицу если не существует Users
            //Со следующими колонками id,name,lastName,age. У каждой колонки задан свой тип данных
            String sql = """
                        CREATE TABLE if not exists Users (
                        id bigint,
                        name varchar,
                        lastName varchar,
                        age smallint
                    );""";
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса Statement,
            // в который передадим наш скрипт
            Statement statement = conn.createStatement();
            //Передаем в него наш скрипт и запускаем его
            statement.execute(sql);
            //Закрываем подключение к бд
            conn.close();
        } catch (Exception e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try {
            //Чтобы удалить таблицу - необходим соответствующий скрипт
            //Удалить таблицу если существует Users
            String sql = """
                    DROP TABLE if exists Users
                    """;
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса Statement,
            // в который передадим наш скрипт
            Statement statement = conn.createStatement();
            //Передаем в него наш скрипт и запускаем его
            statement.execute(sql);
            //Закрываем подключение к бд
            conn.close();
        } catch (Exception e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            //Для того чтобы вставить запись в таблицу, нам необходим соответствующий скрипт
            //Insert into Users - это означает в какую таблицу мы вставляем данные
            //(id, name, lastName, age) - означает в какие колонки этой таблицы происходит вставка
            //VALUES (?,?, ?, ?) - означает какие именно данные подставляются в колонки
            String sql = "INSERT INTO Users (id, name, lastName, age) VALUES (?,?, ?, ?)";
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса PreparedStatement,
            // в который передадим наш скрипт
            //Здесь используется PreparedStatement, а не Statement так как нам необходимо подставить данные
            //а сделать это при помощи Statement невозможно, поэтому исползуем PreparedStatement
            PreparedStatement statement = conn.prepareStatement(sql);
            //Проставляем данным значения по порядку
            statement.setLong(1,new Random().nextLong());
            statement.setString(2, name);
            statement.setString(3, lastName);
            statement.setByte(4, age);
            //Выполняем скрипт, он возвращает количество вставленных строк
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                //Если количество строк больше 0 - это означает что вставка в таблицу произошла успешно
                //Выводим это на экран
                System.out.println("A new user was inserted successfully!");
            }
            //Закрываем подключение к БД
            conn.close();
        } catch (Exception e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            // для того чтобы удалить запись из таблицы - необходим соответсвующий скрипт
            //Удалить из Users где id = ? - под знак вопроса подставится значение переданное нам в параметрах метода
            String sql = """
                    DELETE FROM Users WHERE id=?
                    """;
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса PreparedStatement,
            // в который передадим наш скрипт
            //Здесь используется PreparedStatement, а не Statement так как нам необходимо подставить данные
            //а сделать это при помощи Statement невозможно, поэтому используем PreparedStatement
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);

            int rowsDeleted;
            //запускаем скрипт
            rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                //Запуск скрипта возвращает целочисленное значение,
                // если оно больше 0 - это означает, что запись была удалена
                System.out.println("A user was deleted successfully!");
            }
        } catch (SQLException e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        try {
            //Для того чтобы получить все записи из таблицы Users - необходим соотствествующий скрипт
            //Выбрать *(все) из таблицы USERS
            String sql = "SELECT * FROM Users";
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса Statement,
            // в который передадим наш скрипт
            Statement statement = conn.createStatement();
            //выполняем скрипт, он возвращает экземпляр класса ResultSet,
            // который содержит информацию о полученных данных после выполнения скрипта
            ResultSet resultSet = statement.executeQuery(sql); //сырые данные
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                //пока в resultSet есть следущее значение, мы берем из него данные для
                // создания отдельного экземпляра класса User который соответствует каждой записи в бд
                //Наполняем его данными
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                //Добавляем наполненный экземпляр в список наших юзеров
                users.add(user);
            }
            //Возвращаем список юзеров
            return users;
        } catch (Exception e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            // и выкидываем исключение для завершения работы приложения
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }

    public void cleanUsersTable() {
        try {
            //Для того чтобы очистить таблицу - необходим соответствующий скрипт
            //Удалить из USERS
            String sql = """
                    DELETE FROM Users
                    """;
            //После создания скрипта, необходимо получить подключение к базе данных
            //У нас есть заготовленный метод получения подключения к базе данных
            Connection conn = Util.getConnection();
            //После получения подключения к бд, необходимо передать скрипт, который мы собираемся запустить
            //Для этого мы получаем из экземляра класса Connection экземляр класса Statement,
            // в который передадим наш скрипт
            Statement statement = conn.createStatement();
            //выполняем наш скрипт
            statement.execute(sql);
            //Закрываем соединение с БД
            conn.close();
        } catch (Exception e) {
            //В случае ошибки - выводим список запросов при возникновении этой ошибки
            // и выкидываем исключение для завершения работы приложения
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }
}
