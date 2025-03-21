package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenBank {

    ArrayList<Token> tokens = new ArrayList<>();
    int currentTokenPosition;
    Integer lastTokenPosition;
    Map<String, String> tokenQueries;
    Map<String, Token> tokenToTypeMap;
    String query;

    TokenBank(String command) throws Exception {
        StringTokeniser stringTokeniser = new StringTokeniser();
        ArrayList<String> tokenNames = stringTokeniser.getListOfTokens(command);
        setTokens(tokenNames);
        currentTokenPosition = 0;

        Lexer lexer = new Lexer(this);
        lexer.setup();
    }
    // TokenBank(ArrayList<String> tokenNames) {
    //     setTokens(tokenNames);
    //     currentTokenPosition = 0;
    // }

    void initialise() {
        tokenQueries = createTokenQueries();
        tokenToTypeMap = createTokenTypesToTokens();
        currentTokenPosition = 0;
        query = getCurrentQueryAsTokenTypes();
    }

    Map<String, Token> createTokenTypesToTokens() {
        Map<String, Token> getTokenFromType = new HashMap<>();
        for (Token token : tokens) {
            getTokenFromType.put(token.getTokenType(), token);
        }
        return getTokenFromType;
    }

    boolean tokenQueryIsValid() {
        for (String pattern : tokenQueries.values()) {
            if (pattern.contains(query)) {
                return true;
            }
        }
        return false;
    }

    String getPatternFromTokenQueriesMap(String key) {
        return tokenQueries.get(key);
    }

    Token getTokenFromType(String tokenType) {
        return tokenToTypeMap.get(tokenType);
    }

    boolean tokenExistsInQuery(String tokenType) {
        for (Token token : tokens) {
            if (token.getTokenType().equals(tokenType)) {
                return true;
            }
        }
        return false;
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
            return null;
        }
        currentTokenPosition++;
        return tokens.get(currentTokenPosition);
    }

    void setCurrentTokenToPosition (int position){
        currentTokenPosition = position;
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

    String getCurrentQueryAsTokenTypes() {
        StringBuilder result = new StringBuilder();
        for (Token token : tokens) {
            result.append(token.getTokenType()).append(" ");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
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
            else if (description.equals("valueList") && token.isAValue()) {
                newTokens.add(token);
            }
            else if (description.equals("condition") && (token.isACondition() || token.getTokenType().equals("booleanOperator"))) {
                newTokens.add(token);
            }
            else if (description.equals("nameValuePairs") && token.isNameValuePair()) {
                newTokens.add(token);
            }
            token = nextToken();

        }
        return newTokens;
    }
}
