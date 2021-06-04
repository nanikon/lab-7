package ru.nanikon.FlatCollection.arguments;


import java.io.Serializable;

public class UserArg extends ObjectArgument<String> implements Serializable {
    private String login;
    private String password;
    private boolean isDouble;

    public UserArg(boolean isDouble) {
        this.isDouble = isDouble;
    }

    @Override
    public void setValue(String value) {
    }

    public void setLogin(String value) {
        login = value;
    }

    public String getLogin() { return login; }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDouble() {
        return isDouble;
    }

    /**
     * Clear builder
     */
    @Override
    public void clear() {
    }
}
