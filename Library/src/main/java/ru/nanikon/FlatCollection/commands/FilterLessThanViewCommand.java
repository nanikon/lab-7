package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.ViewArg;
import ru.nanikon.FlatCollection.data.View;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * output elements whose view field value is less than the specified value
 */
public class FilterLessThanViewCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new ViewArg()};
    private String information = "'filter_less_than_view view' - вывести элементы, значение поля view которых меньше заданного";
    private String login;
    private String password;

    public FilterLessThanViewCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        View view = ((ViewArg) params[0]).getValue();
        String result = manager.viewFilteredInfo(view);
        if (result.equals("")) {
            return "Объектов коллекции со значением поля вид меньше, чем " + view + " не найдено";
        }
        return "Объекты коллекции со значением поля вид меньше, чем " + view + "\n" + result;
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
        return "filter_less_than_view";
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
