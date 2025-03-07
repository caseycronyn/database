package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class Attributes extends Parser {
    public ArrayList<String> buildAttributes(TokenBank tokenBank) {
        int position = tokenBank.getCurrentTokenPosition();
        String currentToken;
        ArrayList<String> fragments = new ArrayList<>();
        for (; position < tokenBank.tokens.size(); position++) {
            currentToken = tokenBank.tokens.get(position);
            if (!Arrays.asList(specialCharacters).contains(currentToken)) {
//            System.out.println(tokenBank.tokens.get(position));
                fragments.add(tokenBank.tokens.get(position));
            }
        }
//        String str = String.join("", fragments);
//        System.out.println(fragments);
        return fragments;
    }
}
