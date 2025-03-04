package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

// Maybe use regular expressions for a subset of this
// normalise this input so that it's all the same. takes a string and turns it into an array of token
public class Tokenizer {
    ArrayList<String> tokenArray = new ArrayList<>();

    Tokenizer (String query) {
        tokenArray.addAll(Arrays.asList(query.split(" ")));
//        remove semicolon ';' at the end
        tokenArray.remove(tokenArray.size() - 1);
//        System.out.println(tokenArray);
    }

}
