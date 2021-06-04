package ru.nanikon.FlatCollection.exceptions;


/**
 * Indicates that the number in the executable script contains an error. If you can still ask the user again in interactive mode, then in file mode you need to either skip the command or stop executing it
 */
public class ScriptException extends Exception{
    public ScriptException(String message) {
        super(message);
    }
}
