package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.Client;
import ru.nanikon.FlatCollection.arguments.*;
import ru.nanikon.FlatCollection.commands.Command;
import ru.nanikon.FlatCollection.commands.RegisterCommand;
import ru.nanikon.FlatCollection.exceptions.BooleanInputException;
import ru.nanikon.FlatCollection.exceptions.NotPositiveNumberException;
import ru.nanikon.FlatCollection.exceptions.ScriptException;

import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
 * A parser for command line arguments (as read from the console or from a file). All three methods are static since there is no binding to the instance
 */

public class ArgParser {
    /**
     * Parses an object, asking for and receiving all its fields in turn
     * @param arg - the argument corresponding to the desired object
     * @param scr - active scanner in this moment
     * @param isConsole - whether we are currently working in interactive mode. If with a file - false
     * @param isPartly - do I need to ask before each field whether we will enter it? Set to true for commands such as update
     * @throws ScriptException - called if a script is executed and an incorrect command is found in it
     */
    public static void parseObject(ObjectArgument<?> arg, Scanner scr, boolean isConsole, boolean isPartly) throws ScriptException {
        HashMap<String[], ThrowConsumer<String>> params = arg.getParams();
        arg.clear();
        String[][] fields = params.keySet().toArray(new String[0][0]);
        Arrays.sort(fields, (line1, line2) -> Integer.parseInt(line1[0]) - Integer.parseInt(line2[0]));
        ofer:
        for (String[] param : fields) {
            boolean right;
            if (isPartly) {
                if (isConsole) {
                    System.out.println("Хотите изменить поле " + param[1] + "? Если да, введите +, иначе -");
                    System.out.print(Client.PS2);
                }
                do {
                    String line;
                    try {
                        line = scr.nextLine().trim();
                    } catch (NoSuchElementException e) {
                        if (isConsole) {
                            System.exit(0);
                        }
                        System.out.println("Скрипт закончился некорректно");
                        return;
                    }
                    if (line.equals("+")) {
                        break;
                    } else if (line.equals("-")) {
                        continue ofer;
                    } else {
                        if (isConsole) {
                            System.out.println("Ожидалось +/-, встречено " + line + ". Попробуйте ещё раз.");
                            System.out.print(Client.PS2);
                        } else {
                            throw new ScriptException("Ошибка в скрипте!" + "Ожидалось +/-, встречено " + line);
                        }
                    }
                } while (true);
            }
            if (isConsole) {
                System.out.println(param[0] + " Введите поле " + param[1] + ". " + param[2]);
                System.out.print(Client.PS2);
            }
            do {
                right = true;
                try {
                    params.get(param).accept(scr.nextLine().trim());
                } catch (NumberFormatException e) {
                    if (isConsole) {
                        System.out.println("Ошибка! Введеная строка не является числом! Попробуйте ещё раз");
                        right = false;
                        System.out.print(Client.PS2);
                    } else {
                        throw new ScriptException("Ошибка в скрипте! Ожидалось число, а встречена строка");
                    }
                } catch (NullPointerException | IllegalArgumentException e) {
                    if (isConsole) {
                        System.out.println("Ошибка! " + e.getMessage() + " Попробуйте ещё раз.");
                        System.out.print(Client.PS2);
                        right = false;
                    } else {
                        throw new ScriptException("Ошибка в скрипте! " + e.getMessage());
                    }
                } catch (NotPositiveNumberException | BooleanInputException e) {
                    if (isConsole) {
                        System.out.println("Ошибка! " + e.getMessage() + " Попробуйте ещё раз.");
                        right = false;
                        System.out.print(Client.PS2);
                    } else {
                        throw new ScriptException("Ошибка в скрипте!" + e.getMessage());
                    }
                } catch (NoSuchElementException e) {
                    if (isConsole) {
                        System.exit(0);
                    }
                    System.out.println("Скрипт закончился некорректно");
                    return;
                }
            } while(!right);
        }
        arg.setValue("");
    }

    /**
     * Parses arguments written in the same line as the command
     * @param arg - parses the pim argument, which is entered on the same line as the command. You do not need to ask again in the parser, because in case of incorrect input, you need to re-type the command, so the errors are thrown further
     * @param line - arg as String
     * @throws IOException - called if in execute_script command file isn't found
     */
    public static void parseLine(AbstractArgument<?> arg, String line) throws IOException {
        arg.setValue(line);
    }

    /**
     * Parses the enumeration. On the first attempt, it is assumed that its value is entered in the same line as the command. In case of an error, it outputs the variants of the enumeration values and waits for the input in a new line.
     * @param arg - the argument corresponding to the enumeration
     * @param value - the value read after the command
     * @param scr - whether we are currently working in interactive mode. If with a file - false
     * @param isConsole - do I need to ask before each field whether we will enter it? Set to true for commands such as update
     * @throws ScriptException - called if a script is executed and an incorrect command is found in it
     */
    public static void parseEnum(EnumArgument<?> arg, String value, Scanner scr, boolean isConsole) throws ScriptException {
        boolean right = true;
        do {
            try {
                arg.setValue(value);
                right = true;
            } catch (IllegalArgumentException | NullPointerException e) {
                if (isConsole) {
                    System.out.println("Ошибка! " + e.getMessage() + " Повторите ввод ещё раз, используя следующие варианты:");
                    System.out.println(arg.getConstants());
                    right = false;
                    System.out.print(Client.PS2);
                    value = scr.nextLine().trim();
                } else {
                    throw new ScriptException("В скрипте обнаружена ошибка! " + e.getMessage());
                }
            } catch (NoSuchElementException ex) {
                if (isConsole) {
                    System.exit(0);
                }
                System.out.println("Скрипт закончился некорректно");
                return;
            }
        } while (!right);
    }

    /**
     * Handles the situation when the user needs the answer Yes/No in the form of +/-
     * @return true/false
     */
    public static boolean parseYesNot() {
        do {
            System.out.print(Client.PS2);
            Scanner rightScr = new Scanner(System.in);
            String answer = rightScr.nextLine().trim();
            if (answer.equals("+")) {
                return true;
            } else if (answer.equals("-")) {
                return false;
            } else {
                System.out.println("Ожидалось +/-, а встречено " + answer);
            }
        } while (true);
    }

    public static void parseUser(Command command, Scanner scr, boolean isConsole) throws ScriptException {
        boolean doubles = command instanceof RegisterCommand;
        start:
        while (true) {
            try {
                if (isConsole) {
                    System.out.println("Введите логин:");
                    System.out.print(Client.PS2);
                }
                String login = scr.nextLine().trim();
                if  (isConsole) {
                    System.out.println("Введите пароль:");
                    System.out.print(Client.PS2);
                }
                String password_one = "";
                if (isConsole) {
                    password_one = new String(System.console().readPassword());
                } else {
                    password_one = scr.nextLine().trim();
                }
                if (doubles) {
                    if (isConsole) {
                        System.out.println("Для проверки введите пожалуйста пароль ещё раз");
                        System.out.print(Client.PS2);
                    }
                    String password_two = "";
                    if (isConsole) {
                        password_two = new String(System.console().readPassword());
                    } else {
                        password_two = scr.nextLine().trim();
                    }
                    if (!password_one.equals(password_two) && !isConsole) {
                        throw new ScriptException("Повторение пароля неверное");
                    }
                    int i = 0;
                    while (!password_one.equals(password_two)) {
                        System.out.println("Упс, пароли не совпадают. Попробуйте ещё раз");
                        password_two = new String(System.console().readPassword());
                        i++;
                        if (i == 3) {
                            System.out.println("Кажется, вы забыли, что ввели в первый раз. Давайте начнем с начала");
                            continue start;
                        }
                    }
                }
                command.setLogin(login);
                command.setPassword(password_one);
                return;
            } catch (NoSuchElementException ex) {
                if (isConsole) {
                    System.exit(0);
                }
                System.out.println("Скрипт закончился некорректно");
                return;
            }
        }
    }
}
