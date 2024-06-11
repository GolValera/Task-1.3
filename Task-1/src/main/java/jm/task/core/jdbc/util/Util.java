package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    //Настройка соединения с БД без использования Хибера - чистый JDBC
    public static Connection getConnection() {
        //УРЛ по которому располагается база данных
        String dbURL = "jdbc:postgresql://localhost:5433/testTask";
        //Пользователь БД
        String username = "postgres";
        //пароль от бд
        String password = "password";
        try {
            //Чтобы получить соединение с базой данных
            //Необходимо создать Экземпляр класса Connection
            //Получаем его через статический метод getConnection у класса DriverManager
            //В метод передаем урл, пользователя и пароль
            Connection conn = DriverManager.getConnection(dbURL, username, password);
            if (conn != null) {
                //Если подключение получено - выводим в консоль текст с подтверждением подключения
                System.out.println("Connected");
            }
            //возвращаем экземпляр подключения к БД
            return conn;
        } catch (SQLException ex) {
            //В случае возникновения исключения выводим весь список запросов которые происходили
            ex.printStackTrace();
        }
        //Если подключение не удалось - выбрасываем исключение и завершаем работу программы
        throw new RuntimeException("ERROR");
    }

    public static SessionFactory getSessionFactory() {
        //Для использования Хибернейта необходимо создать фабрику сессий из которых потом берутся сессии,
        // через которые выполняются запросы в бд
        SessionFactory sessionFactory = null;
            try {
                //Необходимо настроить конфигурацию, для того чтобы иметь возмоэность создать фабрику,
                // создаем экземпляр класса Configuration
                Configuration configuration = new Configuration();
                //У конфигурации есть внутри настройки, которые необходимо тоже задать,
                // следовательно, создаем экземпляр класса Properties
                Properties settings = new Properties();
                //Кладем в настройки след параметры
                //Драйвер необходимо указать, для того чтобы дать понять с какой базой работаем
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                //Необходимо указать адрес(УРЛ) по которому располагается база
                settings.put(Environment.URL, "jdbc:postgresql://localhost:5433/testTask");
                //задать имя пользователя
                settings.put(Environment.USER, "postgres");
                //задать пароль
                settings.put(Environment.PASS, "password");
                //задать диалект, который хибернейт будет использовать при создании скриптов в бд
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                //настройка, которая включает/выключает показ скриптов, которые генерирует хибернейт при обращении в базу
                settings.put(Environment.SHOW_SQL, "false");

                //настройка, которая определяет где будет находиться сессия, в данном случае в текущем потоке
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                //настройка, которая определяет, необходимы ли какие-либо действия при старте и завершении работы приложения,
                // в данном случае выбираем update, чтобы хибернейт мог создать свои sequence для успешного запуска, более нам ничего не нужно
                settings.put(Environment.HBM2DDL_AUTO, "update");

                //Кладем настройки в конфигурацию
                configuration.setProperties(settings);

                //добавляем в конфигурацию класс который является нашей таблицей
                configuration.addAnnotatedClass(User.class);

                //Применяем наши настройки, не до конца понятно почему ServiceRegistry - погуглить
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                //используя конфигурацию, вызываем метод построения фабрики сессий
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //возвращаем фабрику сессий
        return sessionFactory;
    }
}
