package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

/**
 * Universal interface for all teams
 */
public interface Command {
    /**
     * running the command
     */
    default String execute(CollectionManager collection) { return ""; }
    default String execute(DBManager manager) { return ""; }

    /**
     * @return - returns the help for the command. For help command
     */
    String getInformation();

    /**
     * @return - returns the list of arguments required for the command to work, which must be obtained from the user
     */
    AbstractArgument<?>[] getArgs();

    //void putArg(String name, AbstractArgument<?> arg);

    String getName();

    String getLogin();
    String getPassword();

    void setLogin(String login);
    void setPassword(String password);
}
