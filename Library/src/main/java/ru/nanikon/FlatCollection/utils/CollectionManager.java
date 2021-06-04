package ru.nanikon.FlatCollection.utils;

import ru.nanikon.FlatCollection.data.Flat;
import ru.nanikon.FlatCollection.data.Transport;
import ru.nanikon.FlatCollection.data.View;
import ru.nanikon.FlatCollection.exceptions.FileCollectionException;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Performs all work with the collection. Depends on the parser of the file with the collection
 */

public class CollectionManager {
    private LinkedList<Flat> flatsCollection = new LinkedList<>();
    private JsonLinkedListParser parser;
    private String saveTime;

    /**
     * Create Manager and load collection
     * @param parser - parser which linked with file with collection
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     * @throws FileCollectionException called if if there is any error in the data and it cannot be converted to the required type
     */
    public CollectionManager(JsonLinkedListParser parser) throws IOException, FileCollectionException {
        this.parser = parser;
        loadCollection();
        saveTime = parser.getSaveTime().toString().substring(0,10) + " " + parser.getSaveTime().toString().substring(11,19);
    }

    /**
     * @return Next id to new element in collection
     */
    public int generateNextId() {
        if (flatsCollection.isEmpty()) {
            return 1;
        } else {
            return flatsCollection.getLast().getId() + 1;
        }
    }

    /**
     * @return Size of the collection
     */
    public int getSize() {
        return flatsCollection.size();
    }

    /**
     * @return Name of the collection's type
     */
    public String getType() { return Flat.class.getName(); }

    public String getCreationDate() throws IOException {
        return parser.getCreationDate().toString().substring(0,10) + " " + parser.getCreationDate().toString().substring(11,19);
    }

    /**
     * @return File's name which linked with this collection
     */
    public String getFileName() { return parser.getPath(); }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public Flat getFirst() {
        try {
            return flatsCollection.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public Flat getLast() {
        try {
            return flatsCollection.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * @param id Id of Flat
     * @return A flat by id or null if flat not found
     */
    public Flat getById(int id) {
        for (Flat flat : flatsCollection) {
            if (flat.getId() == id) {
                return flat;
            }
        }
        return null;
    }

    /**
     * Add a flat tho the end of collection
     * @param flat A flat to add
     */
    public void addLast(Flat flat) {
        flatsCollection.add(flat);
    }

    /**
     * Insert a flat in position
     * @param flat A flat ot insert
     * @param id A position to insert
     */
    public void addById(Flat flat, int id) {
        flatsCollection.add(id, flat);
    }

    /**
     * Replace a flat in position
     * @param flat A flat to replace
     * @param id A position to replace
     */
    public void setById(Flat flat, int id) {
        flatsCollection.set(id, flat);
    }

    /**
     * Remove flat by Id
     * @param id A id to remove
     */
    public void removeById(int id) {
        flatsCollection.remove(id);
    }

    /**
     * Remove first flat which transport field is equal to the specified one
     * @param transport A transport to remove
     */
    public String removeByTransport(Transport transport) {
        int id = flatsCollection.stream().filter(f -> f.getTransport().equals(transport)).map(f -> f.getId()).findFirst().orElse(-1);
        /*int id = -1;
        for (Flat flat : flatsCollection) {
            if (flat.getTransport().equals(transport)) {
                id = flat.getId();
                break;
            }
        }*/
        if (id == -1) {
            return "Не найдено квартир, у которых значение поля транспорт равно " + transport;
        } else {
            String result = getById(id).toLongString();
            flatsCollection.remove(id - 1);
            return "Успешно удален элемент {" + result + "}";
        }
    }

    /**
     * Load collection from json file
     * @throws IOException called if the file does not exist, is unreadable, or does not have enough read permissions
     * @throws FileCollectionException called if if there is any error in the data and it cannot be converted to the required type
     */
    public void loadCollection() throws IOException, FileCollectionException {
        flatsCollection = parser.read();
        if (flatsCollection == null) {
            flatsCollection = new LinkedList<>();
        }
    }

    /**
     * Make collection empty
     */
    public void clearCollection() {
        flatsCollection.clear();
    }

    /**
     * Write collection to the json file
     * @throws IOException called if the file does not exist, is irritable, or does not have enough write permissions
     */
    public void saveCollection() throws IOException {
        parser.write(flatsCollection);
        String now = java.time.ZonedDateTime.now().toString();
        saveTime = now.substring(0, 10) + " " + now.substring(11, 19);
    }

    /**
     * @return Last time when collection saved
     */
    public String getSaveTime() {
        return saveTime;
    }

    /**
     * Sort collection by default
     */
    public void sortCollection() {
        Collections.sort(flatsCollection);
    }

    /**
     * @return All elements of the collection in a string-based human-readable representation
     */
    public String toLongStringWithoutSort() {
        StringBuilder info = new StringBuilder();
        for (Flat flat: flatsCollection) {
            info.append(flat.toLongString()).append("\n");
        }
        String result = info.toString().trim();
        return result;
    }

    public String toLongString() {
        StringBuilder info = new StringBuilder();
        for (Flat flat: flatsCollection.stream().sorted((flat1, flat2) -> (int) (flat1.getArea() - flat2.getArea())).collect(Collectors.toList())) {
            info.append(flat.toLongString()).append("\n");
        }
        String result = info.toString().trim();
        return result;
    }

    /**
     * @return Integer average value for the entire collection
     */
    public int getAverageNumberOfRooms() {
        if (getSize() == 0) {
            return 0;
        }
        long result = flatsCollection.stream().mapToLong(flat -> flat.getNumberOfRooms()).sum();
        result = result / getSize();
        return (int) result;
    }

    /**
     * @param view A view to filter
     * @return Elements of the collection which field view equals to the specified one. Return in a string-based human-readable representation
     */
    public String viewFilteredInfo(View view) {
        StringBuilder result = new StringBuilder();
        for (Flat flat : flatsCollection.stream().filter((flat) -> flat.getView().compareTo(view) < 0).sorted((flat1, flat2) -> (int) (flat1.getArea() - flat2.getArea())).collect(Collectors.toList())) {
            result.append(flat.toLongString()).append("\n");
        }
        return result.toString().trim();
    }
}

