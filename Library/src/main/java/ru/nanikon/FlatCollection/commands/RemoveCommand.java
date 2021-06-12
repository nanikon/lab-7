package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.IntArg;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * delete an item from the collection by its id
 */
public class RemoveCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new IntArg()};
    private String information = "'remove_by_id id' - удалить элемент из коллекции по его id";
    private String login;
    private String password;

    public RemoveCommand() {
    }

    /**
     * running the command
     */
    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        int id = ((IntArg) params[0]).getValue();
        return manager.deleteById(id, login);
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
        return "remove_by_id";
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
