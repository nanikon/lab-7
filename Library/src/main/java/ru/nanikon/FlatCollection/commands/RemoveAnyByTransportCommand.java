package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.TransportArg;
import ru.nanikon.FlatCollection.data.Transport;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * remove one element from the collection whose transport field value is equivalent to the specified one
 */
public class RemoveAnyByTransportCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new TransportArg()};
    private String information = "'remove_any_by_transport transport' - удалить из коллекции один элемент, значение поля transport которого эквивалентно заданному";
    private String login;
    private String password;

    public RemoveAnyByTransportCommand() {
    }

    /**
     * running the command
     */
    @Override
    public String execute(DBManager manager) {
        Transport transport = ((TransportArg) params[0]).getValue();
        return manager.deleteByTransport(transport.name(), login);
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
        return "remove_any_by_transport";
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

