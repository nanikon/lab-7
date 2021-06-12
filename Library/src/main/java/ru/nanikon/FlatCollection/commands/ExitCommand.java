package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.exceptions.StopConnectException;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.IOException;
import java.io.Serializable;

/**
 * Terminates the execution of a script or all programs. Does not save the collection to a file!
 */
public class ExitCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'exit' - завершить программу";
    private String login;
    private String password;

    public ExitCommand() {
    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        try {
            collection.saveCollection();
        } catch (IOException e) {
            return "Перед завершением программы не удалось сохранить коллекцию в файл. Нам очень не хочется, чтобы вы все потеряли, поэтому проверьте связанный с коллекцией файл и попробуйте ещё раз.";
        }
        throw new StopConnectException();
    }

    public String execute(DBManager manager) {
        manager.save();
        return "Работа программы завершена. Коллекция была сохранена";
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
        return "exit";
    }

    /**
     * @return - returns the help for the command. For help command
     */
    @Override
    public String getInformation() {
        return information;
    }

    public String getEnd() {return "Работа программы завершена. Коллекция была сохранена"; }

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
