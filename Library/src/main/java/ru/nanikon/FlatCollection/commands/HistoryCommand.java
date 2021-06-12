package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * output the last 8 commands (without their arguments)
 */
public class HistoryCommand implements Command, Serializable {
    Deque<String> history = new ArrayDeque<>();
    private AbstractArgument<?>[] params = {};
    private String information = "'history' - вывести последние 8 команд (без их аргументов)";
    private String login;
    private String password;

    public HistoryCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        StringBuilder result = new StringBuilder();
        for (String command: history) {
            if (!(command == null)) {
                result.append(command).append("\n");
            }
        }
        return result.toString().trim();
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
        return "history";
    }

    /**
     * @return - returns the help for the command. For help command
     */
    @Override
    public String getInformation() {
        return information;
    }

    public void putCommand(String nameCommand) {
        if (history.size() == 8) {
            String lastCommand = history.remove();
        }
        history.add(nameCommand);
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
