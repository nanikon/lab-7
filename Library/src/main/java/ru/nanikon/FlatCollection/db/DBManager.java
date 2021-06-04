package ru.nanikon.FlatCollection.db;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.FlatBuilder;
import ru.nanikon.FlatCollection.data.House;
import ru.nanikon.FlatCollection.data.View;
import ru.nanikon.FlatCollection.exceptions.BooleanInputException;

import static java.sql.ResultSet.*;

public class DBManager {
    private final String URL;
    private final String login;
    private final String password;
    private Connection connection;
    private LinkedList<Flat> collection = new LinkedList<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);


    public DBManager(String URL, String login, String password) {
        this.URL = URL;
        this.login = login;
        this.password = password;
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, login, password);
            connection.setAutoCommit(false);
            System.out.println("Соединение установлено");
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            System.err.println("Не удалось подключиться к базе данных. Завершение работы.");
            System.exit(-1);
        }
    }

    public String registerUser(String login, String password) {
        String salt = generateSalt();
        Lock wlock = lock.writeLock();
        wlock.lock();
        String result = "";
        try (PreparedStatement statement = connection.prepareStatement(Requests.INSERT_USER.QUERY)) {
            statement.setString(1, login);
            statement.setString(2, encryptPassword(password + salt));
            statement.setString(3, salt);
            if (statement.executeUpdate() == 1) {
                connection.commit();
                return "Пользователь " + login + " успешно зарегистрирован";
            } else {
                connection.rollback();
                return "Регистрация не удалась. Повторите попытку позднее";
            }
        } catch (SQLException e) {
            //result = "Пользователь с логином " + login + " уже существует";
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            return "Пользователь с логином " + login + " уже существует";
        } finally {
            wlock.unlock();
        }
    }

    public String encryptPassword(String password) {
        String pepper = "lerug7^%q43r";
        return DigestUtils.md5Hex(pepper + password);
    }

    public String generateSalt() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST1234567890!@#$%^*()_+".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public String checkUser(String login, String password) {
        Lock rlock = lock.readLock();
        rlock.lock();
        try (PreparedStatement statement = connection.prepareStatement(Requests.SELECT_USER.QUERY)) {
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return "Пользователь с логином " + login + " не найден";
            }
            if (result.getString("password").equals(encryptPassword(password + result.getString("salt")))) {
                return "Вы успешно авторизированы как " + login;
            } else {
                return "Упс, пароль не угадали!";
            }
        } catch (SQLException e) {
            return "При авторизации возникли проблемы. Повторите попытку позднее";
        } finally {
            rlock.unlock();
        }
    }

    public boolean checkLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(Requests.SELECT_USER.QUERY)) {
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ignored) {}
    }

    public void save() {
        try {
            connection.commit();
        } catch (SQLException ignored) {
        }
    }

    public String addFlat(Flat flat, String login) {
        int house_id;
        int view_id;
        int transport_id;
        int user_id;
        Lock wlock = lock.writeLock();
        wlock.lock();
        try {
            house_id = getHouseId(flat.getHouse());
            if (house_id == -1) {
                PreparedStatement houseStatement = connection.prepareStatement(Requests.INSERT_HOUSE.QUERY);
                houseStatement.setString(1, flat.getHouseName());
                if (flat.getYear() == null) {
                    houseStatement.setNull(2, java.sql.Types.NULL);
                } else {
                    houseStatement.setLong(2, flat.getYear());
                }
                if (flat.getNumberOfFloors() == null) {
                    houseStatement.setNull(3, java.sql.Types.NULL);
                } else {
                    houseStatement.setInt(3, flat.getNumberOfFloors());
                }
                houseStatement.executeUpdate();
                house_id = getHouseId(flat.getHouse());
            }
            //house.next();
            //house_id = house.getInt("id");

            PreparedStatement viewStatement = connection.prepareStatement(Requests.SELECT_VIEW.QUERY);
            viewStatement.setString(1, flat.getView().name());
            ResultSet view = viewStatement.executeQuery();
            view.next();
            view_id = view.getInt("id");

            PreparedStatement trStatement = connection.prepareStatement(Requests.SELECT_TRANSPORT.QUERY);
            trStatement.setString(1, flat.getTransport().name());
            ResultSet tr = trStatement.executeQuery();
            tr.next();
            transport_id = tr.getInt("id");

            PreparedStatement userStatement = connection.prepareStatement(Requests.SELECT_USER.QUERY);
            userStatement.setString(1, login);
            ResultSet user = userStatement.executeQuery();
            user.next();
            user_id = user.getInt("id");

            PreparedStatement flatStatement = connection.prepareStatement(Requests.INSERT_FLAT.QUERY);
            flatStatement.setString(1, flat.getName());
            flatStatement.setDouble(2, flat.getX());
            flatStatement.setDouble(3, flat.getY());
            flatStatement.setTimestamp(4, Timestamp.valueOf(flat.getCreationDate().toLocalDateTime()));
            flatStatement.setLong(5, flat.getArea());
            flatStatement.setInt(6, flat.getNumberOfRooms());
            flatStatement.setBoolean(7, flat.isCentralHeating());
            flatStatement.setInt(8, house_id);
            flatStatement.setInt(9, transport_id);
            flatStatement.setInt(10, view_id);
            flatStatement.setInt(11, user_id);
            flatStatement.executeUpdate();
            connection.commit();
            initialCollection();
            //return "Элемент {" + flat.toLongString() + "} успешно добавлен в коллекцию";
            return "Элемент успешно добавлен в коллекцию";
        } catch (SQLException e) {
            //e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return "При добавлении объекта произошла ошибка";
        } catch (Exception e) {
            return "Не удалось добавить объект, так как кто-то сломал БД";
        } finally {
            wlock.unlock();
        }
    }

    public int getHouseId(House house) {
        int result = -1;
        try (PreparedStatement houseSelectStatement = connection.prepareStatement(Requests.SELECT_HOUSE.QUERY)) {
            houseSelectStatement.setString(1, house.getName());
            if (house.getYear() == null) {
                houseSelectStatement.setNull(2, java.sql.Types.NULL);
            } else {
                houseSelectStatement.setLong(2, house.getYear());
            }
            if (house.getNumberOfFloors() == null) {
                houseSelectStatement.setNull(3, java.sql.Types.NULL);
            } else {
                houseSelectStatement.setInt(3, house.getNumberOfFloors());
            }
            ResultSet houseRes = houseSelectStatement.executeQuery();
            if (houseRes.next()) {
                result = houseRes.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readAll() {
        String result = "";
        try (PreparedStatement flats = connection.prepareStatement(Requests.SELECT_FLAT.QUERY)) {
            connection.commit();
            result = loadCollection(flats.executeQuery());
        } catch (SQLException e) {
            result = "Прочитать данные из БД не удалось, попробуйте позднее";
        } catch(Exception e) {
            result = "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        }
        return result;
    }

    public String readSort() {
        String result = "";
        try (PreparedStatement flats = connection.prepareStatement(Requests.SELECT_SORT_FLAT.QUERY)) {
            result = loadCollection(flats.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            result = "Прочитать данные из БД не удалось, попробуйте позднее";
        } catch(Exception e) {
            result = "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        }
        return result;
    }

    public String readFilter(String filter) {
        String result = "";
        try (PreparedStatement flats = connection.prepareStatement(Requests.SELECT_FILTER_FLAT.QUERY)) {
            flats.setString(1, filter);
            result = loadCollection(flats.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            result = "Прочитать данные из БД не удалось, попробуйте позднее";
        } catch(Exception e) {
            result = "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        }
        return result;
    }

    public String viewFilteredInfo(View view) {
        Lock rlock = lock.readLock();
        rlock.lock();
        StringBuilder result = new StringBuilder();
        for (Flat flat : collection.stream().filter((flat) -> flat.getView().compareTo(view) < 0).sorted((flat1, flat2) -> (int) (flat1.getArea() - flat2.getArea())).collect(Collectors.toList())) {
            result.append(flat.toLongString()).append("\n");
        }
        rlock.unlock();
        return result.toString().trim();
    }

    public void initialCollection() throws SQLException, BooleanInputException {
        PreparedStatement flats = connection.prepareStatement(Requests.SELECT_FLAT.QUERY);
        String result = loadCollection(flats.executeQuery());
        connection.commit();
    }

    public String loadCollection(ResultSet result1) throws SQLException, BooleanInputException {
        collection.clear();
        try {
            while (result1.next()) {
                FlatBuilder builder = new FlatBuilder();
                builder.reset();
                builder.setId(result1.getString("id"));
                builder.setName(result1.getString("name"));
                builder.setX(result1.getString("x"));
                builder.setY(result1.getString("y"));
                builder.setCreationDate(ZonedDateTime.ofInstant(result1.getTimestamp("creationdate").toInstant(), ZoneId.of("UTC")));
                builder.setArea(result1.getString("area"));
                builder.setNumberOfRooms(result1.getString("numberofrooms"));
                builder.setCentralHeating(result1.getBoolean("centralheating") ? "+" : "-");
                builder.setHouseName(result1.getString("house_name"));
                builder.setYear(result1.getString("year"));
                builder.setNumberOfFloors(result1.getString("numberoffloors"));
                builder.setView(result1.getString("view_value"));
                builder.setTransport(result1.getString("transport_value"));
                collection.add(builder.getResult());
            }
        } catch (Exception e) {
            throw e;
        }
        return toLongString();
    }

    public String toLongString() {
        Lock rlock = lock.readLock();
        rlock.lock();
        StringBuilder info = new StringBuilder();
        for (Flat flat: collection) {
            info.append(flat.toLongString()).append("\n");
        }
        String result = info.toString().trim();
        rlock.unlock();
        return result;
    }

    public String sortCollection() {
        Lock rlock = lock.readLock();
        rlock.lock();
        Collections.sort(collection);
        rlock.unlock();
        return toLongString();
    }

    public String clear() {
        Lock wlock = lock.writeLock();
        wlock.lock();
        try (PreparedStatement result = connection.prepareStatement(Requests.CLEAR.QUERY)) {
            result.executeUpdate();
            connection.commit();
            collection.clear();
            return "Коллекция успешно очищена";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return "Не удалось очистить коллекцию";
        } finally {
            wlock.unlock();
        }
    }

    public String getAverage() {
        Lock rlock = lock.readLock();
        rlock.lock();
        try (PreparedStatement flats = connection.prepareStatement(Requests.AVERAGE_ROOMS.QUERY)) {
            ResultSet results = flats.executeQuery();
            results.next();
            return String.valueOf(results.getDouble("average_room"));
        } catch (SQLException e) {
            return "Прочитать данные из БД не удалось, попробуйте позднее";
        } catch(Exception e) {
            return "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        } finally {
            rlock.unlock();
        }
    }

    public int getAverageNumberOfRooms() {
        Lock rlock = lock.readLock();
        rlock.lock();
        if (getSize().equals("0")) {
            return 0;
        }
        long result = collection.stream().mapToLong(Flat::getNumberOfRooms).sum();
        result = result / Integer.parseInt(getSize());
        rlock.unlock();
        return (int) result;
    }

    public String getSize() {
        return String.valueOf(collection.size());
    }

    public String deleteById(int id,String login) {
        String result = "";
        Lock wlock = lock.writeLock();
        wlock.lock();
        try (PreparedStatement results = connection.prepareStatement(Requests.DELETE_FLAT.QUERY)) {
            results.setInt(1, id);
            results.setString(2, login);
            if (results.executeUpdate() == 0) {
                result = "Не удалось удалить элемент: он не принадлежит вам, или не найден стаким id";
            } else {
                result = "Элемент успешно удален";
            }
            connection.commit();
            initialCollection();
        } catch (SQLException e) {
            //e.printStackTrace();
            result = "Не удалось удалить элемент по неизвестной причине. Попробуйте позднее";
        } catch (Exception e) {
            return "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        } finally {
            wlock.unlock();
        }
        return result;
    }

    public String deleteByTransport(String transport, String login) {
        String result = "";
        Lock wlock = lock.writeLock();
        wlock.lock();
        try {
            PreparedStatement flats = connection.prepareStatement(Requests.SELECT_TRANSPORT_FLAT.QUERY);
            flats.setString(1, login);
            flats.setString(2, transport);
            ResultSet result1 = flats.executeQuery();
            if (!result1.next()) { return "У вас нет элемента с таким видом транспорта"; }
            PreparedStatement result2 = connection.prepareStatement(Requests.DELETE_FLAT.QUERY);
            result2.setInt(1, result1.getInt("id"));
            result2.setString(2, login);
            if (result2.executeUpdate() == 0) {
                result = "Не удалось удалить элемент: он не принадлежит вам, или не найден стаким id";
            } else {
                result = "Элемент успешно удален";
            }
            connection.commit();
            initialCollection();
        } catch (SQLException e) {
            //e.printStackTrace();
            result = "Не удалось удалить элемент по неизвестной причине. Попробуйте позднее";
        } catch (Exception e) {
            return "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        } finally {
            wlock.unlock();
        }
        return result;
    }

    public String update(int id, String login, FlatBuilder builder) {
        String result = "";
        Lock wlock = lock.writeLock();
        wlock.lock();
        try (PreparedStatement flatStatement = connection.prepareStatement(Requests.SELECT_FLAT_ID.QUERY, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE)) {
            flatStatement.setInt(1, id);
            flatStatement.setString(2, login);
            ResultSet flat = flatStatement.executeQuery();
            if (!flat.next()) { return "Элемента с таким id не существует или он не ваш (ха-ха)"; }
            HashSet<String> newFields = builder.getChange();
            System.out.println(flat.getInt("id"));
            //flat.updateInt("flat_id", id);
            if (newFields.contains("name")) { flat.updateString("name", builder.getName()); }
            if (newFields.contains("x")) { flat.updateDouble("x", builder.getX()); }
            if (newFields.contains("y")) { flat.updateDouble("y", builder.getY()); }
            flat.updateTimestamp(4, Timestamp.valueOf(builder.getCreationDate().toLocalDateTime()));
            if (newFields.contains("area")) { flat.updateLong("area", builder.getArea()); }
            if (newFields.contains("numberOfRooms")) { flat.updateLong("numberofrooms", builder.getNumberOfRooms()); }
            if (newFields.contains("centralHeating")) { flat.updateBoolean(7, builder.isCentralHeating()); }
            if (newFields.contains("view")) {
                PreparedStatement viewStatement = connection.prepareStatement(Requests.SELECT_VIEW.QUERY);
                viewStatement.setString(1, builder.getView().name());
                ResultSet view = viewStatement.executeQuery();
                view.next();
                int view_id = view.getInt("id");
                flat.updateInt("view_id", view_id);
            }
            if (newFields.contains("transport")) {
                PreparedStatement trStatement = connection.prepareStatement(Requests.SELECT_TRANSPORT.QUERY);
                trStatement.setString(1, builder.getTransport().name());
                ResultSet tr = trStatement.executeQuery();
                tr.next();
                int transport_id = tr.getInt("id");
                flat.updateInt("transport_id", transport_id);
            }
            flat.updateRow();
            result = "Элемент успешно обновлен";
            connection.commit();
            initialCollection();
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            result = "Не удалось обносить элемент по непонятной причине. Повторите попытку позднее";
        } catch (Exception e) {
            return "Там это, БД сломали, глянь посмотри: " + e.getMessage();
        } finally {
            wlock.unlock();
        }
        return result;
    }
}
