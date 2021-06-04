package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.FlatArg;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.FlatBuilder;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Adds an object to the collection
 */
public class AddCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new FlatArg()};
    private String information = "'add {element}' - добавить новый элемент в коллекцию";
    private HashMap<String, AbstractArgument<?>> args;

    private String login;
    private String password;

    public AddCommand() {

    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        FlatBuilder builder = ((FlatArg) params[0]).getBuilder();
        try {
            builder.setId(String.valueOf(collection.generateNextId()));
        } catch (NotPositiveNumberException e) {
            System.out.println("Этой ошибки быть не должно, id генерится автоматически");
        }
        Flat flat = builder.getResult();
        collection.addLast(flat);
        return "Элемент {" + flat.toLongString() + "} успешно добавлен в коллекцию";
    }

    public String execute(DBManager manager) {
        FlatBuilder builder = ((FlatArg) params[0]).getBuilder();
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
        return "add";
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

    @Override
    public String getInformation() {
        return information;
    }


}

