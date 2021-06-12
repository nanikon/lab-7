package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.IOException;
import java.io.Serializable;

/**
 * output information about the collection (type, initialization date, number of elements) to the standard output stream.)
 */
public class InfoCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {};
    private String information = "'info' - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)";
    private String login;
    private String password;

    public InfoCommand() {

    }

    /**
     * running the command
     * @return String - information about collection
     */
    @Override
    public String execute(CollectionManager collection) {
        StringBuilder result = new StringBuilder();
        result.append("Информация о коллекции: ").append("\n");
        result.append("тип хранимых объектов: ").append(collection.getType()).append("\n");
        result.append("количество элементов: ").append(collection.getSize()).append("\n");
        result.append("файл, связанный с коллекцией: ").append(collection.getFileName()).append("\n");
        try {
            result.append("дата и время инициализации: ").append(collection.getCreationDate()).append("\n");
        } catch (IOException e) {
            result.append("Файла, связанного с коллекцией не существует. Проверьте его наличие и попробуйте ещё раз.").append("\n");
        }
        result.append("дата и время последнего сохранения в файл: ").append(collection.getSaveTime());
        return result.toString();
    }

    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        StringBuilder result = new StringBuilder();
        result.append("Информация о коллекции: ").append("\n");
        result.append("тип хранимых объектов: ").append(Flat.class.getName()).append("\n");
        result.append("количество элементов: ").append(manager.getSize());
        return result.toString();
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
        return "info";
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
