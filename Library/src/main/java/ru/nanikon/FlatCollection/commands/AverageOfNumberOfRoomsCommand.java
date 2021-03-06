package ru.nanikon.FlatCollection.commands;


import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * Calculates the average value of the numberOfRooms field for the entire collection
 */
public class AverageOfNumberOfRoomsCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'average_of_number_of_rooms' - вывести среднее значение поля numberOfRooms для всех элементов коллекции";
    private String login;
    private String password;

    public AverageOfNumberOfRoomsCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        return "Среднее значение поля количество комнат по всем квартирам коллекции: " + manager.getAverageNumberOfRooms();
    }

    /**
     * @return - returns the list of arguments required for the command to work, which must be obtained from the user
     */
    @Override
    public AbstractArgument<?>[] getArgs() {
        return params;
    }

    @Override
    public String getName() {
        return "average_of_number_of_rooms";
    }

    /**
     * @return - returns the help for the command. For help command
     */
    @Override
    public String getInformation() {
        return information;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
