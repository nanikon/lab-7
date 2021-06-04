package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * sort the collection in the natural order
 */
public class SortCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'sort' - отсортировать коллекцию в естественном порядке";
    private String login;
    private String password;

    public SortCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        collection.sortCollection();
        String result = collection.toLongStringWithoutSort();
        if (result.equals("")) {
            result = "Коллекция пуста, и сортировать нечего...";
        } else {
            result = "Коллекция успешно отсортированна:\n" + result;
        }
        return result;
    }

    @Override
    public String execute(DBManager manager) {
        String result = manager.readSort();
        if (result.equals("")) {
            result = "Коллекция пуста, и сортировать нечего...";
        } else {
            result = "Коллекция успешно отсортированна:\n" + result;
        }
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
        return "sort";
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

