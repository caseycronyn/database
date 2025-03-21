package edu.uob;

// will decide what kind of token each string is
public class Lexer {
    TokenBank tokenBank;

    Lexer(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
        setup();
    }

    void checkForNullTokens() throws NullPointerException {
        for (Token token : tokenBank.getTokens()) {
            if (token.getTokenType() == null) {
                throw new NullTokenException("Token with null type found. Token details: " + token.getValue());
            }
        }
    }

    // should return a hashmap of tokens to set in tokenbank or an error if something is wrong. sets token types
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
        token.nameToUpperCase();
        if (token.isCommandType()) {
            // next one
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
        addPlainTextToken("tableOneName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("and", "AND");
        tokenBank.nextToken();
        addPlainTextToken("tableTwoName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("on", "ON");
        tokenBank.nextToken();
        addPlainTextToken("attributeOneName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("and", "AND");
        tokenBank.nextToken();
        addPlainTextToken("attributeTwoName");
    }

    void deleteLexer() {
        addKeywordTokenIfEquals("from", "FROM");
        tokenBank.nextToken();
        addPlainTextToken("tableName");
        tokenBank.nextToken();
        conditionLexer();
    }

    void updateLexer() {
        addPlainTextToken("tableName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("set", "SET");
        Token token = tokenBank.nextToken();
        while (!(token.getValue().equals("WHERE")) && (tokenBank.numberOfTokensLeft() > 1)) {
            nameValuePair();
            token = tokenBank.getCurrentToken();
        }
        conditionLexer();
    }

    void nameValuePair() {
        Token token = tokenBank.getCurrentToken();
        if (token.getValue().equals(",")) {
            token.setTokenType("comma");
            tokenBank.nextToken();
        }
        addPlainTextToken("attributeName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("equals", "=");
        token = tokenBank.nextToken();
        token.setValueTokenType();
        tokenBank.nextToken();
    }

    void selectLexer() {
        tokenBank.getCurrentToken();
        getAttributeNames();
        Token token = tokenBank.getCurrentToken();
        token.nameToUpperCase();
        if (token.getValue().equals("FROM")) {
            token.setTokenType("from");
        }
        tokenBank.nextToken();
        addPlainTextToken("tableName");
        token = tokenBank.nextToken();
        token.nameToUpperCase();
        if (token.getValue().equals("WHERE")) {
            token.setTokenType("where");
            conditionLexer();
            // condition!! :(( AHHH!!
        }
    }

    void conditionLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpperCase();
        if (token.getValue().equals("WHERE")) {
            token.setTokenType("where");
            // stripParenthesesToEnd();
            parenthesesLexer();
            while (!tokenBank.checkIfAtFinalToken()) {
                token = tokenBank.nextToken();
                if (token.getTokenType() == null) {
                    if (token.isBooleanOperator()) {
                        token.setTokenType("booleanOperator");
                    } else if (tokenBank.numberOfTokensLeft() > 2) {
                        attributeComparatorValue();
                    }
                }
            }
        }
    }


        void attributeComparatorValue () {
            addPlainTextToken("attributeName");
            Token token = tokenBank.nextToken();
            if (token.isComparator()) {
                token.setTokenType("comparator");
            }
            token = tokenBank.nextToken();
            token.setValueTokenType();
        }


        void getAttributeNames() {
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

        void insertLexer () {
            addKeywordTokenIfEquals("into", "INTO");
            tokenBank.nextToken();
            addPlainTextToken("tableName");
            tokenBank.nextToken();
            addKeywordTokenIfEquals("values", "VALUES");
            tokenBank.incrementCurrentTokenPosition();
            parenthesesLexer();
            setNullTokensInParentheses("valueList");
        }

        void alterLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            if (token.getValue().equals("TABLE")) {
                token.setTokenType("tableSelector");
            }
            token = tokenBank.nextToken();
            if (token.isPlainText()) {
                token.setTokenType("tableName");
            }
            token = tokenBank.nextToken();
            token.nameToUpperCase();
            if (checkIfAlterationType(token)) {
                token.setTokenType("alterationType");
            }
            token = tokenBank.nextToken();
            if (token.isPlainText()) {
                token.setTokenType("attributeName");
            }
        }

        void dropLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
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

        void createLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            switch (token.getValue()) {
                case "DATABASE":
                    token.setTokenType("databaseSelector");
                    tokenBank.nextToken();
                    createDatabaseLexer();
                    break;
                case "TABLE":
                    token.setTokenType("tableSelector");
                    tokenBank.nextToken();
                    createTable();
                    break;
            }
        }

        void createDatabaseLexer () {
            Token token = tokenBank.getCurrentToken();
            if (token.isPlainText()) {
                token.setTokenType("databaseName");
            }
        }

        void createTable () {
            Token token = tokenBank.getCurrentToken();
            if (token.isPlainText()) {
                token.setTokenType("tableName");
            }
            if (tokenBank.tokens.size() > 4) {
                tokenBank.nextToken();
                parenthesesLexer();
                setNullTokensInParentheses("attributeName");
            }
        }

        // lexers all parentheses
        void parenthesesLexer () {
            boolean parenthesesOpen = false;
            int openParenthesisCount = 0;
            int closeParenthesisCount = 0;
            Token token = tokenBank.getCurrentToken();
            int initialTokenPosition = tokenBank.getCurrentTokenPosition();
            // token = tokenBank.nextToken();
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

        void setNullTokensInParentheses (String description){
            Token token = tokenBank.getCurrentToken();
            while (!token.getTokenType().equals("closeParenthesis")) {
                token = tokenBank.nextToken();
                if (description.equals("valueList")) {
                    token.setValueTokenType();
                } else if (token.isPlainText()) {
                    token.setTokenType(description);
                }
            }
        }

        void useLexer () {
            Token token = tokenBank.getCurrentToken();
            if (token.isPlainText()) {
                token.setTokenType("databaseName");
            }
        }

        void addPlainTextToken (String description){
            Token token = tokenBank.getCurrentToken();
            if (token.isPlainText()) {
                token.setTokenType(description);
            }
        }

        void addKeywordTokenIfEquals (String description, String pattern){
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            if (token.getValue().equals(pattern)) {
                token.setTokenType(description);
            }
        }

        boolean checkIfAlterationType (Token token){
            return (token.getValue().equals("DROP") || token.getValue().equals("ADD"));
        }

        void setup () {
            lexTokens();
            tokenBank.initialise();
            checkForNullTokens();
        }
}

