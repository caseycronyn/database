package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenBank {
    ArrayList<Token> tokens = new ArrayList<>();
    int currentTokenPosition;
    String currentTable;
    Integer lastTokenPosition;
    Map<String, String> tokenQueries;
    Map<String, Token> tokenToTypeMap;

    TokenBank(ArrayList<String> tokenNames) {
        setTokens(tokenNames);
        currentTokenPosition = 0;
    }

    void initialise() {
        tokenQueries = createTokenQueries();
        tokenToTypeMap = createTokenTypesToTokens();
        currentTokenPosition = 0;
    }

    Map<String, Token> createTokenTypesToTokens() {
        Map<String, Token> getTokenFromType = new HashMap<>();
        for (Token token : tokens) {
            getTokenFromType.put(token.getTokenType(), token);
        }
        return getTokenFromType;
    }


    String getQueryAsTokenTypes(String key) {
        return tokenQueries.get(key);
    }

    Token getTokenFromType(String tokenType) {
        return tokenToTypeMap.get(tokenType);
    }

    Map<String, String> createTokenQueries() {
        Map<String, String> tokenKeys = new HashMap<>();
        tokenKeys.put("use", "useCommand databaseName terminator");
        tokenKeys.put("create database", "createCommand databaseSelector databaseName terminator");
        tokenKeys.put("create table",  "createCommand tableSelector tableName terminator");
        tokenKeys.put("create table with attributes", "createCommand tableSelector tableName");
        tokenKeys.put("drop database", "dropCommand databaseSelector databaseName terminator");
        tokenKeys.put("drop table", "dropCommand tableSelector tableName terminator");
        tokenKeys.put("alter", "alterCommand tableSelector tableName alterationType attributeName terminator");
        tokenKeys.put("insert", "insertCommand into tableName values");
        tokenKeys.put("select", "attributeName from tableName");
        tokenKeys.put("select all", "selectCommand wildAttributeSymbol from tableName");
        tokenKeys.put("select with condition", "selectCommand wildAttributeSymbol from tableName ... ");
        tokenKeys.put("update", "updateCommand tableName set attributeName equals");
        tokenKeys.put("delete", "deleteCommand from tableName where");
        tokenKeys.put("join", "joinCommand tableOneName and tableTwoName on attributeOneName and attributeTwoName terminator");
        return tokenKeys;
    }

    int getLastTokenPosition() {
        return lastTokenPosition;
    }

    boolean checkIfAtFinalToken() {
        return (lastTokenPosition == currentTokenPosition);
    }

    int numberOfTokensLeft() {
        return tokens.size() - currentTokenPosition - 1;
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
        lastTokenPosition = tokens.size() - 1;
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
        if (currentTokenPosition >= tokens.size() - 1) {
            System.out.println("No more tokens");
            return null;
        }
        currentTokenPosition++;
        return tokens.get(currentTokenPosition);
    }

    Token previousToken () {
        currentTokenPosition--;
        return tokens.get(currentTokenPosition);
    }

    void setCurrentTokenToPosition (int position){
        currentTokenPosition = position;
    }

    void deleteToken(int tokenPosition) {
        tokens.remove(tokenPosition);
        lastTokenPosition--;
        // update indices after token
        for (int i = tokenPosition; i < tokens.size(); i++) {
            tokens.get(i).setPosition(i);
        }
    }

    void printTokenNames () {
        for (Token token : tokens) {
            System.out.printf(token.getValue() + " ");
        }
        System.out.println();
    }

    void printTokenTypes() {
        for (Token token : tokens) {
            System.out.printf(token.getTokenType() + " ");
        }
        System.out.println();
    }

    String getQueryAsTokenTypes() {
        StringBuilder result = new StringBuilder();
        for (Token token : tokens) {
            result.append(token.getTokenType()).append(" ");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    Token getTokenAtPosition(int position) {
        return tokens.get(position);
    }


    public List<Token> getTokenTypeFromFragment(String description, String startToken, String endToken) {
        int start = getTokenFromType(startToken).getPosition();
        setCurrentTokenToPosition(start + 1);
        Token token = getCurrentToken();
        List<Token> newTokens = new ArrayList<>();
        while (!token.getTokenType().equals(endToken)) {
            if (token.getTokenType().equals(description)) {
                newTokens.add(token);
            }
            else if (description.equals("valueList") && tokenIsAValue(token)) {
                newTokens.add(token);
            }
            else if (description.equals("condition") && (tokenIsACondition(token) || token.getTokenType().equals("booleanOperator"))) {
                newTokens.add(token);
            }
            else if (description.equals("nameValuePairs") && tokenIsNameValuePair(token)) {
                newTokens.add(token);
            }
            token = nextToken();

        }
        return newTokens;
    }

    boolean tokenIsNameValuePair (Token token) {
        return token.getTokenType().equals("attributeName") || token.getTokenType().equals("equals") || tokenIsAValue(token);
    }



    boolean tokenIsParenthesis (Token token) {
        return token.getTokenType().equals("openParenthesis") || token.getTokenType().equals("closeParenthesis");
    }

    boolean tokenIsACondition(Token token) {
        return (token.getTokenType().equals("attributeName") || token.getTokenType().equals("comparator") || tokenIsAValue(token));
    }


    public boolean tokenIsAValue(Token token) {
        return (token.getTokenType().contains("Literal") || token.getTokenType().equals("NULL"));
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
