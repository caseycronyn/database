package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

// Maybe use regular expressions for a subset of this
// normalise this input so that it's all the same. takes a string and turns it into an array of token
public class StringTokeniser {
    String[] specialCharacters = {"(", ")", ",", ";", "==", ">", "<", ">=", "<=", "!=", "LIKE"};
    ArrayList<String> tokens = new ArrayList<String>();

    // Treats text inside single quotes as a single token
    // Breaks non-quoted text on spaces and special punctuation
    // Assembles all tokens into a list and prints them out
    public ArrayList<String> getListOfTokens(String query) {
        // Split the query on single quotes (to separate out query text from string literals)
        String[] fragments = query.split("'");
        for (int i = 0; i < fragments.length; i++) {
            // Every other fragment is a string literal, so just add it straight to "result" token list
            if (i % 2 != 0) tokens.add("'" + fragments[i] + "'");
                // If it's not a string literal, it must be query text (which needs further processing)
            else {
                // Tokenise the fragment into an array of strings - this is the "clever" bit !
                String[] nextBatchOfTokens = tokeniseFragment(fragments[i]);
                // Then copy all the tokens into the "result" list (needs a bit of conversion)
                tokens.addAll(Arrays.asList(nextBatchOfTokens));
            }
        }
        // Finally, loop through the result array list, printing out each token a line at a time
        // for (int i = 0; i < tokens.size(); i++) System.out.println(tokens.get(i));
        return tokens;
    }


    String[] tokeniseFragment(String input) {
        // Add in some extra padding spaces either side of the "special characters"...
        // so we can be SURE that they are separated by AT LEAST one space (possibly more)
        for (int i = 0; i < specialCharacters.length; i++) {
            input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        // Remove any double spaces (the previous padding activity might have introduced some of these)
        while (input.contains("  ")) input = input.replace("  ", " "); // Replace two spaces by one
        // Remove any whitespace from the beginning and the end that might have been introduced
        input = input.trim();
        // Finally split on the space char (since there will now ALWAYS be a SINGLE space between tokens)
        return input.split(" ");
    }

}
