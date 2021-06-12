package ru.nanikon.FlatCollection.commands;

import ru.nanikon.FlatCollection.arguments.AbstractArgument;
import ru.nanikon.FlatCollection.arguments.FlatArg;
import ru.nanikon.FlatCollection.arguments.IntArg;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.FlatBuilder;
import ru.nanikon.FlatCollection.db.DBManager;
import ru.nanikon.FlatCollection.exceptions.BooleanInputException;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;
import ru.nanikon.FlatCollection.utils.CollectionManager;

import java.io.Serializable;
import java.util.HashSet;

public class UpdateCommand implements Command, Serializable {
    private AbstractArgument<?>[] params = {new IntArg(), new FlatArg()};
    private String information = "'update id {element}' - обновить значение элемента коллекции, id которого равен заданному";

    private String login;
    private String password;

    public UpdateCommand() {
        //((IdArg) params[0]).setCollection(collection);
    }

    /**
     * running the command
     */
    @Override
    public String execute(CollectionManager collection) {
        try {
            int id = ((IntArg) params[0]).getValue();
            Flat oldFlat = collection.getById(id);
            FlatBuilder oldBuilder = ((FlatArg) params[1]).getBuilder();
            FlatBuilder newBuilder = new FlatBuilder();
            HashSet<String> newFields = oldBuilder.getChange();
            newBuilder.setId(String.valueOf(oldFlat.getId()));
            newBuilder.setCreationDate(oldFlat.getCreationDate());
            newBuilder.setName(newFields.contains("name") ? oldBuilder.getName() : oldFlat.getName());
            newBuilder.setX(newFields.contains("x") ? String.valueOf(oldBuilder.getX()) : String.valueOf(oldFlat.getX()));
            newBuilder.setY(newFields.contains("y") ? String.valueOf(oldBuilder.getY()) : String.valueOf(oldFlat.getY()));
            newBuilder.setArea(newFields.contains("area") ? String.valueOf(oldBuilder.getArea()) : String.valueOf(oldFlat.getArea()));
            newBuilder.setNumberOfRooms(newFields.contains("numberOfRooms") ? String.valueOf(oldBuilder.getNumberOfRooms()) : String.valueOf(oldFlat.getNumberOfRooms()));
            String builderCentralHeating = oldBuilder.isCentralHeating() ? "+" : "-";
            String flatCentralHeating = oldFlat.isCentralHeating() ? "+" : "-";
            newBuilder.setCentralHeating(newFields.contains("centralHeating") ? builderCentralHeating : flatCentralHeating);
            newBuilder.setView(newFields.contains("view") ? oldBuilder.getView().name() : oldFlat.getView().name());
            newBuilder.setTransport(newFields.contains("transport") ? oldBuilder.getTransport().name() : oldFlat.getTransport().name());
            newBuilder.setHouseName(newFields.contains("nameHouse") ? oldBuilder.getHouseName() : oldFlat.getHouseName());
            newBuilder.setYear(newFields.contains("year") ? String.valueOf(oldBuilder.getYear()) : String.valueOf(oldFlat.getYear()));
            newBuilder.setNumberOfFloors(newFields.contains("numberOfFloors") ? String.valueOf(oldBuilder.getNumberOfFloors()) : String.valueOf(oldFlat.getNumberOfFloors()));
            Flat newFlat = newBuilder.getResult();
            collection.setById(newFlat, id - 1);
            return "Успешно обновлен элемент: {" + newFlat.toLongString() + "}";
        } catch (NotPositiveNumberException| BooleanInputException e) {
            return "ошибка во входных данных";
        } catch (NullPointerException e) {
            return "Элемента с таким id не найдено. Проверьте его и повторите команду ещё раз";
        }
    }

    @Override
    public String execute(DBManager manager) {
        if (!manager.chekUser(login, password)) {
            return "Ой, вы там в приложении что-то напортачили и мы то ли логин не найдем, то ли пароль для него не тот. Перезайдите нормально!";
        }
        int id = ((IntArg) params[0]).getValue();
        FlatBuilder oldBuilder = ((FlatArg) params[1]).getBuilder();
        return manager.updateById(id, login, oldBuilder);
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
        return "update";
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
