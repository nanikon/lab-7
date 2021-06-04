package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.FlatArg;
import ru.nanikon.FlatCollection.arguments.IntArg;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.FlatBuilder;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;

/**
 * добавить новый элемент в заданную позицию
 */
public class InsertCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new IntArg(), new FlatArg()};
    private String information = "'insert_at index {element}' - добавить новый элемент в заданную позицию";
    private String login;
    private String password;

    public InsertCommand() {
    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        int id = ((IntArg) params[0]).getValue();
        FlatBuilder builder = ((FlatArg) params[1]).getBuilder();
        try {
            builder.setId(String.valueOf(collection.generateNextId()));
        } catch (NotPositiveNumberException e) {
            System.out.println("Этой ошибки быть не должно, так как id генерируется автоматически");
        }
        Flat flat = builder.getResult();
        try {
            collection.addById(flat, id - 1);
        } catch (IndexOutOfBoundsException e) {
            return "Невозможно вставить элемент на позицию " + id + " так как размер коллекции всего " + collection.getSize() + " и такой позиции пока не существует";
        }
        return "В коллекцию на позицию " + id + " успешно добавлен элемент {" + flat.toLongString() + "}";
    }

    @Override
    public String execute(DBManager manager) {
        FlatBuilder builder = ((FlatArg) params[1]).getBuilder();
        return manager.addFlat(builder.getResult(), login);
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
        return "insert_at";
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
