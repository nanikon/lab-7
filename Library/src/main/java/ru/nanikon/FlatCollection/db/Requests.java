package ru.nanikon.FlatCollection.db;

public enum Requests {
    AVERAGE_ROOMS("SELECT AVG(numberOfRooms) as average_room FROM flats"),
    INSERT_FLAT("INSERT INTO flats (name, x, y, creationDate, area, numberOfRooms, centralHeating, house_id, transport_id, view_id, user_id) values ((?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?))"),
    SELECT_TRANSPORT("SELECT id FROM transports WHERE transport_value = ?"),
    SELECT_VIEW("SELECT id FROM views WHERE view_value = ?"),
    SELECT_HOUSE("SELECT * FROM houses WHERE house_name = ? AND year = ? AND numberOfFloors = ?"),
    INSERT_HOUSE("INSERT INTO houses (house_name, year, numberOfFloors) values ((?), (?), (?))"),
    SELECT_FLAT("SELECT * FROM flats JOIN houses ON flats.house_id = houses.id JOIN views ON flats.view_id = views.id JOIN transports ON flats.transport_id = transports.id"),
    SELECT_SORT_FLAT("SELECT * FROM flats JOIN houses ON flats.house_id = houses.id JOIN views ON flats.view_id = views.id JOIN transports ON flats.transport_id = transports.id ORDER BY area"),
    SELECT_FILTER_FLAT("SELECT * FROM flats JOIN houses ON flats.house_id = houses.id JOIN views ON flats.view_id = views.id JOIN transports ON flats.transport_id = transports.id WHERE view_id <= (SELECT id from views WHERE view_value = ?)"),
    SELECT_TRANSPORT_FLAT("SELECT id FROM flats WHERE user_id = (SELECT id FROM users WHERE login = ?) and transport_id = (SELECT id FROM transports WHERE transport_value = ?)"),
    SELECT_FLAT_ID("SELECT * FROM flats WHERE id = ? and user_id = (SELECT users.id FROM users WHERE login = ?)"),
    SELECT_USER("SELECT * FROM users WHERE login = ?"),
    INSERT_USER("INSERT INTO users (login, password, salt) values((?), (?), (?))"),
    DELETE_FLAT("DELETE FROM flats WHERE id = ? AND user_id = (SELECT id FROM users WHERE login = ?)"),
    UPDATE_FLAT("UPDATE flats SET name = ?, x = ?, y = ?, area = ?, numberOfRooms = ?, centralHeating = ?, transport_id = (SELECT id FROM transports WHERE transport_value = ?), view_id = (SELECT id from views WHERE view_value = ?), house_id = ? WHERE id = ? AND user_id = (SELECT users.id FROM users WHERE login = ?)"),
    CLEAR("DELETE FROM flats WHERE user_id = (SELECT id FROM users WHERE login = ?)");
    String QUERY;

    Requests(String QUERY) {
        this.QUERY = QUERY;
    }
}
