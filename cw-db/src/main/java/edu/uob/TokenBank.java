package edu.uob;

import java.util.ArrayList;

public enum TokenBank {
    INSTANCE;
    private int currentToken = 0;
    private final ArrayList<String> tokens = new ArrayList<>();

    static void addTokens(ArrayList<String> tokenArray) {
        tokens.addAll(tokenArray);
    }

    public String getNextToken() {
        currentToken++;
        return tokens.get(currentToken);
    }
}
