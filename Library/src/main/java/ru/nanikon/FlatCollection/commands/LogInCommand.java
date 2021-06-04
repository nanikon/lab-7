package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.UserArg;
import ru.nanikon.FlatCollection.db.DBManager;

import java.io.Serializable;

public class LogInCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'log_in login password' - авторизироваться";
    private String login;
    private String password;
    @Override
    public String execute(DBManager manager) {
        return manager.checkUser(login, password);
    }

    /**
     * @return - returns the help for the command. For help command
     */
    @Override
    public String getInformation() {
        return information;
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
        return "log_in";
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
