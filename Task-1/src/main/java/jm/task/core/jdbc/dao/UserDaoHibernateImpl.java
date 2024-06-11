package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        //В случае создания таблицы необходимо воспользоваться нативными запросами,
        // так как хибернейт не поддерживает данный вид команд
        String sql = """
                    CREATE TABLE if not exists Users (
                    id bigint,
                    name varchar,
                    lastName varchar,
                    age smallint
                )""";
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //выполняем нативный запрос
        currentSession.createNativeQuery(sql).executeUpdate();
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
    }

    @Override
    public void dropUsersTable() {
        //В случае удаления таблицы необходимо воспользоваться нативными запросами,
        // так как хибернейт не поддерживает данный вид команд
        String sql = """
                DROP TABLE if exists Users
                """;
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //выполняем нативный запрос
        currentSession.createNativeQuery(sql).executeUpdate();
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //Сессия дает доступ к работе с БД,
        // она же специальными методами создает скрипты и исполняет их
        currentSession.save(new User(name, lastName, age));
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
    }

    @Override
    public void removeUserById(long id) {
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //Сессия дает доступ к работе с БД,
        // она же специальными методами создает скрипты и исполняет их
        currentSession.delete(new User(id));
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
    }

    @Override
    public List<User> getAllUsers() {
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //Для получения всех записей из таблицы, необходимо подготовить запрос,
        // к сожалению я не нашла иного способа кроме как через нативный запрос
        List<User> users = currentSession.createNativeQuery("select * from users", User.class).getResultList();
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
        //Возвращаем результат выборки из таблицы
        return users;
    }

    @Override
    public void cleanUsersTable() {
        //Для создания запросов в БД необходимо получить фабрику сессий,
        // для этого заранее подготовлен специальный метод
        SessionFactory sessionFactory = Util.getSessionFactory();
        //Необходимо из фабрики сессий достать текущую сессию, чтобы в последствии открыть транзакцию
        Session currentSession = sessionFactory.getCurrentSession();
        //Открываем транзакцию
        Transaction transaction = currentSession.beginTransaction();
        //Для удаления всех записей из таблицы, необходимо подготовить запрос,
        // к сожалению я не нашла иного способа кроме как через нативный запрос
        currentSession.createNativeQuery("delete from Users").executeUpdate();
        //Подтверждаем транзакцию, чтобы сохранить в БД результат скрипта
        transaction.commit();
        //закрываем фабрику сессий, ввиду окончания работы с ней
        sessionFactory.close();
    }
}
