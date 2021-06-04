package ru.nanikon.FlatCollection.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.exceptions.FileCollectionException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Parses the LinkedList collection to a json file and back
 */

public class JsonLinkedListParser {
    private final String path;
    private final Type type;

    /**
     * Create Parser. You need one parser to one file.
     * @param path - path to file which linked with this parser
     */
    public JsonLinkedListParser(String path) {
        this.path = path;
        type = new TypeToken<LinkedList<Flat>>(){}.getType();
    }

    /**
     * @return Path to file which linked with this parser
     */
    public String getPath() {return path;}

    /**
     * write collection to a file
     * @param data LinkedList&lt;Flat&gt; collection which you want to write to a file
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     */
    public void write(LinkedList<Flat> data) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))
        // BufferedWriter writer = new BufferedWriter(new FileWriter(path))
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(json);
        } catch (IOException e) {
            throw new IOException("Невозможно получить доступ к файлу", e);
        }
    }

    /**
     * reads a collection from a file
     * @return LinkedList&lt;Flat&gt; - the collection that is stored in the file
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     * @throws FileCollectionException called if if there is any error in the data and it cannot be converted to the required type
     */
    public LinkedList<Flat> read() throws IOException, FileCollectionException {
        StringBuilder json = new StringBuilder();
        try (Scanner scr = new Scanner(new File(path), "UTF-8")) {
            while (scr.hasNextLine()) {
                json.append(scr.nextLine());
            }
        } catch (IOException e) {
            throw new IOException("Не удается найти или прочитать файл", e);
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Flat.class, new FlatJsonConverter()).registerTypeAdapter(type, new ListJsonConverter()).create();
        try {
            LinkedList<Flat> result = gson.fromJson(String.valueOf(json), type);
            return result;
        } catch (JsonSyntaxException e) {
            throw new FileCollectionException("В файле не найдены объекты коллекции и/или объекты, являющиеся их частью, или же сам файл некорректен");
        } catch (Exception e) {
            throw new FileCollectionException("В файле содержится ошибка: " + e.getMessage());
        }
    }

    /**
     * @return date and time when the collection file was created
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     */
    public FileTime getCreationDate() throws IOException {
        return Files.readAttributes(new File(path).toPath(), BasicFileAttributes.class).creationTime();
    }

    /**
     * @return date and time when was the collection last saved to a file
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     */
    public FileTime getSaveTime() throws IOException {
        return Files.readAttributes(new File(path).toPath(), BasicFileAttributes.class).lastModifiedTime();
    }
}
