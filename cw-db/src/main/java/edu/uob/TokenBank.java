package edu.uob;

import java.util.ArrayList;

public class TokenBank {
    ArrayList<Token> tokens = new ArrayList<>();
    int currentTokenPosition;
    String currentTable;
    Integer lastTokenPosition;


    TokenBank(ArrayList<String> tokenNames) {
        setTokens(tokenNames);
        lastTokenPosition = tokens.size() - 1;
    }

    int getLastTokenPosition() {
        return lastTokenPosition;
    }

    Token getFirstToken() {
        return tokens.get(0);
    }

    Token getLastToken() {
        return tokens.get(tokens.size() - 1);
    }

    String getCurrentTable() {
        return currentTable;
    }

    void setCurrentTable(String currentTable) {
        this.currentTable = currentTable;
    }

    TokenBank() {
        currentTokenPosition = 0;
    }

    void setTokens(ArrayList<String> tokenNames) {
        for (int i = 0; i < tokenNames.size(); i++) {
            Token token = new Token(tokenNames.get(i), i);
            tokens.add(token);
        }
    }

    Token getCurrentToken () {
        return tokens.get(currentTokenPosition);
    }

    Integer getCurrentTokenPosition () {
        return currentTokenPosition;
    }

    void incrementCurrentTokenPosition () {
        currentTokenPosition += 1;
    }

    ArrayList<Token> getTokens () {
        return tokens;
    }

    Token nextToken () {
        currentTokenPosition++;
        return tokens.get(currentTokenPosition);
    }

    void setTokenAtPosition (int position){
        currentTokenPosition = position;
    }

    void printTokenNames () {
        for (Token token : tokens) {
            System.out.printf(token.getName() + " ");
        }
        System.out.println();
    }

    void printTokenTypes() {
        for (Token token : tokens) {
            System.out.printf(token.getTokenType() + " ");
        }
        System.out.println();
    }

    Token getTokenAtPosition(int position) {
        return tokens.get(position);
    }


//    ArrayList<String> tokenArray = new ArrayList<>();

//    Tokenizer (String query) {
//        tokenArray.addAll(Arrays.asList(query.split(" ")));
////        remove semicolon ';' at the end
//        tokenArray.remove(tokenArray.size() - 1);
////        System.out.println(tokenArray);
//    }

//    String query = "  INSERT  INTO  people   VALUES(  'Simon Lock'  ,35, 'simon@bristol.ac.uk' , 1.8  ) ; ";

//    public static void main(String [] args) {
//        Test t = new Test();
//        t.setup();
//    }

}
