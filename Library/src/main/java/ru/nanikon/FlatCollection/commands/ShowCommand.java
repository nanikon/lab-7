package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;
import java.util.HashMap;

/**
 * output all elements of the collection in a string representation to the standard output stream
 */

public class ShowCommand implements Command, Serializable {
    //private CollectionManager collection;
    private AbstractArgument<?>[] params = {};
    private String information = "'show' - вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    private HashMap<String, AbstractArgument<?>> args;
    private String login;
    private String password;

    public ShowCommand() {
    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        if (collection.getSize() == 0) {
            return "Коллекция пустая";
        }
        return collection.toLongString();
    }

    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        String result = manager.toLongString();
        if (result.equals("")) { result = "Коллекция пустая"; }
        return result;
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
        return "show";
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
