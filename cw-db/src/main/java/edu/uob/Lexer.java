package edu.uob;

// will decide what kind of token each string is
public class Lexer {
    TokenBank tokenBank;

    Lexer(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
        initialiseLexer();
    }

    void initialiseLexer() {
        lexTokens();
        tokenBank.initialise();
        checkNullTokens();
    }

    void checkNullTokens() throws NullPointerException {
        for (Token token : tokenBank.getTokens()) {
            if (token.getTokenType() == null) {
                throw new NullTokenException("Token with null type found. Token details: " + token.getValue());
            }
        }
    }

    void lexTokens() {
        semicolonLexer();
        commandTypeLexer();
    }

    void semicolonLexer() {
        if (tokenBank.getLastToken().getValue().equals(";")) {
            tokenBank.getLastToken().setTokenType("terminator");
        }
    }

    void commandTypeLexer() {
        Token token = tokenBank.getFirstToken();
        token.nameToUpper();
        if (token.isCommandType()) {
            switch (token.getValue()) {
                case "CREATE":
                    token.setTokenType("createCommand");
                    tokenBank.nextToken();
                    createLexer();
                    break;
                case "USE":
                    token.setTokenType("useCommand");
                    tokenBank.nextToken();
                    useLexer();
                    break;
                case "DROP":
                    token.setTokenType("dropCommand");
                    tokenBank.nextToken();
                    dropLexer();
                    break;
                case "ALTER":
                    token.setTokenType("alterCommand");
                    tokenBank.nextToken();
                    alterLexer();
                    break;
                case "INSERT":
                    token.setTokenType("insertCommand");
                    tokenBank.nextToken();
                    insertLexer();
                    break;
                case "SELECT":
                    token.setTokenType("selectCommand");
                    tokenBank.nextToken();
                    selectLexer();
                    break;
                case "UPDATE":
                    token.setTokenType("updateCommand");
                    tokenBank.nextToken();
                    updateLexer();
                    break;
                case "DELETE":
                    token.setTokenType("deleteCommand");
                    tokenBank.nextToken();
                    deleteLexer();
                    break;
                case "JOIN":
                    token.setTokenType("joinCommand");
                    tokenBank.nextToken();
                    joinLexer();
                    break;
            }
        }
    }

    void joinLexer() {
        plainTextLexer("tableOneName");
        tokenBank.nextToken();
        keyWordLexer("and", "AND");
        tokenBank.nextToken();
        plainTextLexer("tableTwoName");
        tokenBank.nextToken();
        keyWordLexer("on", "ON");
        tokenBank.nextToken();
        plainTextLexer("attributeOneName");
        tokenBank.nextToken();
        keyWordLexer("and", "AND");
        tokenBank.nextToken();
        plainTextLexer("attributeTwoName");
    }

    void deleteLexer() {
        keyWordLexer("from", "FROM");
        tokenBank.nextToken();
        plainTextLexer("tableName");
        tokenBank.nextToken();
        conditionLexer();
    }

    void updateLexer() {
        plainTextLexer("tableName");
        tokenBank.nextToken();
        keyWordLexer("set", "SET");
        Token token = tokenBank.nextToken();
        while (!(token.getValue().equals("WHERE")) && (tokenBank.getTokensLeft() > 1)) {
            nameValueLexer();
            token = tokenBank.getCurrentToken();
        }
        conditionLexer();
    }

    void nameValueLexer() {
        Token token = tokenBank.getCurrentToken();
        if (token.getValue().equals(",")) {
            token.setTokenType("comma");
            tokenBank.nextToken();
        }
        plainTextLexer("attributeName");
        tokenBank.nextToken();
        keyWordLexer("equals", "=");
        token = tokenBank.nextToken();
        token.setValueType();
        tokenBank.nextToken();
    }

    void selectLexer() {
        tokenBank.getCurrentToken();
        attributeLexer();
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        if (token.getValue().equals("FROM")) {
            token.setTokenType("from");
        }
        tokenBank.nextToken();
        plainTextLexer("tableName");
        token = tokenBank.nextToken();
        token.nameToUpper();
        if (token.getValue().equals("WHERE")) {
            token.setTokenType("where");
            conditionLexer();
        }
    }

    void conditionLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        if (token.getValue().equals("WHERE")) {
            token.setTokenType("where");
            parenthesesLexer();
            valuePairLexer();
        }
    }

    void valuePairLexer() {
        while (!tokenBank.checkIfAtEnd()) {
            Token token = tokenBank.nextToken();
            if (token.getTokenType() == null) {
                if (token.isBooleanOperator()) {
                    token.setTokenType("booleanOperator");
                } else if (tokenBank.getTokensLeft() > 2) {
                    attributeComparatorValue();
                }
            }
        }
    }


    void attributeComparatorValue() {
        plainTextLexer("attributeName");
        Token token = tokenBank.nextToken();
        if (token.isComparator()) {
            token.setTokenType("comparator");
        }
        token = tokenBank.nextToken();
        token.setValueType();
    }


    void attributeLexer() {
        Token token = tokenBank.getCurrentToken();
        while (!token.getValue().equalsIgnoreCase("FROM")) {
            if (token.isWildAttributeList()) {
                token.setTokenType("attributeName");
            } else if (token.getValue().equals(",")) {
                token.setTokenType("comma");
            }
            token = tokenBank.nextToken();
        }
    }

    void insertLexer() {
        keyWordLexer("into", "INTO");
        tokenBank.nextToken();
        plainTextLexer("tableName");
        tokenBank.nextToken();
        keyWordLexer("values", "VALUES");
        tokenBank.incrementCurrentTokenPosition();
        parenthesesLexer();
        enclosedTokenLexer("valueList");
    }

    void alterLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        if (token.getValue().equals("TABLE")) {
            token.setTokenType("tableSelector");
        }
        token = tokenBank.nextToken();
        if (token.isPlainText()) {
            token.setTokenType("tableName");
        }
        token = tokenBank.nextToken();
        token.nameToUpper();
        if (alterationLexer(token)) {
            token.setTokenType("alterationType");
        }
        token = tokenBank.nextToken();
        if (token.isPlainText()) {
            token.setTokenType("attributeName");
        }
    }

    void dropLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        switch (token.getValue()) {
            case "DATABASE":
                token.setTokenType("databaseSelector");
                token = tokenBank.nextToken();
                if (token.isPlainText()) {
                    token.setTokenType("databaseName");
                }
                break;
            case "TABLE":
                token.setTokenType("tableSelector");
                token = tokenBank.nextToken();
                if (token.isPlainText()) {
                    token.setTokenType("tableName");
                }
                break;
        }
    }

    void createLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        switch (token.getValue()) {
            case "DATABASE":
                token.setTokenType("databaseSelector");
                tokenBank.nextToken();
                createDatabaseLexer();
                break;
            case "TABLE":
                token.setTokenType("tableSelector");
                tokenBank.nextToken();
                createTableLexer();
                break;
        }
    }

    void createDatabaseLexer(){
        Token token = tokenBank.getCurrentToken();
        if (token.isPlainText()) {
            token.setTokenType("databaseName");
        }
    }

    void createTableLexer(){
        Token token = tokenBank.getCurrentToken();
        if (token.isPlainText()) {
            token.setTokenType("tableName");
        }
        if (tokenBank.tokens.size() > 4) {
            tokenBank.nextToken();
            parenthesesLexer();
            enclosedTokenLexer("attributeName");
        }
    }

    void parenthesesLexer () {
        boolean parenthesesOpen = false;
        int openParenthesisCount = 0;
        int closeParenthesisCount = 0;
        Token token = tokenBank.getCurrentToken();
        int initialTokenPosition = tokenBank.getCurrentTokenPosition();
        for (int i = initialTokenPosition; i < tokenBank.getLastTokenPosition(); i++) {
            if (token.getValue().equals("(")) {
                token.setTokenType("openParenthesis");
                parenthesesOpen = true;
                openParenthesisCount++;
            }
            else if (token.getValue().equals(",")) {
                token.setTokenType("comma");
            } else if ((token.getValue().equals(")")) && parenthesesOpen) {
                token.setTokenType("closeParenthesis");
                closeParenthesisCount++;
            }
            token = tokenBank.nextToken();
        }
        if (openParenthesisCount != closeParenthesisCount) {
            throw new UnbalancedParenthesesException("Unbalanced parentheses detected in Lexer ");
        }
        tokenBank.setCurrentTokenToPosition(initialTokenPosition);
    }

    void enclosedTokenLexer(String description){
        Token token = tokenBank.getCurrentToken();
        while (!token.getTokenType().equals("closeParenthesis")) {
            token = tokenBank.nextToken();
            if (description.equals("valueList")) {
                token.setValueType();
            } else if (token.isPlainText()) {
                token.setTokenType(description);
            }
        }
    }

    void useLexer() {
        Token token = tokenBank.getCurrentToken();
        if (token.isPlainText()) {
            token.setTokenType("databaseName");
        }
    }

    void plainTextLexer(String description){
        Token token = tokenBank.getCurrentToken();
        if (token.isPlainText()) {
            token.setTokenType(description);
        }
    }

    void keyWordLexer(String description, String pattern){
        Token token = tokenBank.getCurrentToken();
        token.nameToUpper();
        if (token.getValue().equals(pattern)) {
            token.setTokenType(description);
        }
    }

    boolean alterationLexer(Token token){
        return (token.getValue().equals("DROP") || token.getValue().equals("ADD"));
    }

}

