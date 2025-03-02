package edu.uob;

import java.util.*;

//populates the tables rows and columns by passing in the formatted string.
//the columns are ordered
public class Table {
    ArrayList<String> attributes = new ArrayList<>();
    ArrayList<String> entries = new ArrayList<>();

    public void populateAttributes(String command) {
        String [] attributeArray = command.split("\t");
        attributes.addAll(List.of(attributeArray));
//        System.out.println(attributes);
    }

    public void populateEntries(ArrayList<String> commandHolder) {
//        loop through each line:
//            split words into seperate terms
//            add to word_array
//            loop through word_array:
//                add each key value pair to a new row

//        for (String entry: commandHolder) {
//            String [] valuesArray = entry.split("\t");
//        }
    }
}
