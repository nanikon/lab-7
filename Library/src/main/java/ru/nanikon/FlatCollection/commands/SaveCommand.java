package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.IOException;
import java.io.Serializable;

/**
 * save the collection to a file
 */
public class SaveCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'save' - сохранить коллекцию в файл";
    private String login;
    private String password;

    public SaveCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        try {
            collection.saveCollection();
            return "Коллекция успешно сохранена";
        } catch (IOException e) {
            return "Невозможно открыть на запись файл, к которому привязана коллекция. проверьте его и попробуйте ещё раз";
        }
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
        return "save";
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
