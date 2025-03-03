package edu.uob;

import java.lang.reflect.Array;
import java.util.*;

//populates the tables rows and columns by passing in the formatted string.
//the columns are ordered
public class Table {
    ArrayList<String> attributes = new ArrayList<>();
    ArrayList<HashMap<String, String>> entries = new ArrayList<>();

    public void populateAttributes(String command) {
        String[] attributeArray = command.split("\t");
        attributes.addAll(List.of(attributeArray));
//        System.out.println(attributes);
    }

    public void populateEntriesAndMapAttributes(ArrayList<String> commandHolder) {
//        loop through each line:
//            split words into separate terms
//            add to word_array
//            loop through word_array:
//                add each key value pair to a new row
        for (int i = 1; i < commandHolder.size(); i++) {
            String[] entryArray = commandHolder.get(i).split("\t");
            HashMap<String, String> row = new HashMap<>();
            if (entryArray.length != attributes.size()) {
                return;
            }
            for (int j = 0; j < attributes.size(); j++) {
                row.put(attributes.get(j), entryArray[j]);
            }
            entries.add(row);
//            System.out.println(row);
//            System.out.println(commandHolder.get(i));
        }
        for (String entry: commandHolder) {
            System.out.println(entry);
        }

    }

}
