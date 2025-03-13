package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// will decide what kind of token each string is
public class Lexer {
    String[] whiteSpaceSymbols, nonWhiteSpaceSymbols, commandArray, commandTypeArray, tokenisedQuery, symbolArray, comparatorArray;
    String commandType, plainText, query, tableOrDatabase, alterationType, stringLiteral, booleanLiteral, floatLiteral, integerLiteral, symbol, wildAttributeList, comparator, parentheses, booleanOperator;
    TokenBank tokenBank;

    Lexer(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
        setup();
        lexTokens();
        checkForNullTokens();
    }

    void checkForNullTokens() {
        for (Token token : tokenBank.getTokens()) {
            if (token.getTokenType() == null) {
                System.out.print("token types: ");
                tokenBank.printTokenTypes();
                tokenBank.printTokenNames();
                throw new java.lang.Error("error has occurred during lexing of tokens. Some are null");
            }
        }
    }

    // should return a hashmap of tokens to set in tokenbank or an error if something is wrong. sets token types
    void lexTokens() {
        semicolonLexer();
        commandTypeLexer();
    }

    void semicolonLexer() {
        if (tokenBank.getLastToken().getName().equals(";")) {
            tokenBank.getLastToken().setTokenType("terminator");
        }
    }

    void commandTypeLexer() {
        Token token = tokenBank.getFirstToken();
        token.nameToUpperCase();
        if (checkTokenForPattern(token, commandType)) {
            // next one
            switch (token.getName()) {
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
        addPlainTextToken("tableName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("and", "AND");
        tokenBank.nextToken();
        addPlainTextToken("tableName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("on", "ON");
        tokenBank.nextToken();
        addPlainTextToken("attributeName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("and", "AND");
        tokenBank.nextToken();
        addPlainTextToken("attributeName");

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
        while (!(token.getName().equals("WHERE")) && (tokenBank.numberOfTokensLeft() > 1)) {
            nameValuePair();
            token = tokenBank.getCurrentToken();
        }
        conditionLexer();
    }

    void nameValuePair() {
        addPlainTextToken("attributeName");
        tokenBank.nextToken();
        addKeywordTokenIfEquals("equals", "=");
        tokenBank.nextToken();
        addTokenIfValue();
        tokenBank.nextToken();
    }

    void selectLexer() {
        tokenBank.getCurrentToken();
        getWildAttributeList();
        Token token = tokenBank.getCurrentToken();
        token.nameToUpperCase();
        if (token.getName().equals("FROM")) {
            token.setTokenType("from");
        }
        tokenBank.nextToken();
        addPlainTextToken("tableName");
        token = tokenBank.nextToken();
        token.nameToUpperCase();
        if (token.getName().equals("WHERE")) {
            token.setTokenType("where");
            conditionLexer();
            // condition!! :(( AHHH!!
        }
    }

    void conditionLexer() {
        Token token = tokenBank.getCurrentToken();
        token.nameToUpperCase();
        if (token.getName().equals("WHERE")) {
            token.setTokenType("where");
            stripParenthesesToEnd();
            while (!tokenBank.checkIfAtFinalToken()) {
                token = tokenBank.nextToken();
                if (checkTokenForPattern(token, booleanOperator)) {
                    token.setTokenType("booleanOperator");
                } else if (tokenBank.numberOfTokensLeft() > 2) {
                    attributeComparatorValue();
                }
            }
        }
    }

        void attributeComparatorValue () {
            addPlainTextToken("attributeName");
            tokenBank.nextToken();
            addTokenIfPatternCheckSuccessful("comparator", comparator);
            tokenBank.nextToken();
            addTokenIfValue();
        }

        void addTokenIfPatternCheckSuccessful (String description, String pattern){
            Token token = tokenBank.getCurrentToken();
            if (checkTokenForPattern(token, pattern)) {
                token.setTokenType(description);
            }
        }

        void getWildAttributeList () {
            Token token = tokenBank.getCurrentToken();
            while (!token.getName().equalsIgnoreCase("FROM")) {
                if (checkTokenForPattern(token, wildAttributeList)) {
                    if (token.getName().equals("*")) {
                        token.setTokenType("wildAttributeSymbol");
                    } else token.setTokenType("attributeName");
                } else if (token.getName().equals(",")) {
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
            setNullTokensInParentheses("variables");
        }

        void alterLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            if (token.getName().equals("TABLE")) {
                token.setTokenType("tableSelector");
            }
            token = tokenBank.nextToken();
            if (checkTokenIsPlainText(token)) {
                token.setTokenType("tableName");
            }
            token = tokenBank.nextToken();
            token.nameToUpperCase();
            if (checkIfAlterationType(token)) {
                token.setTokenType("alterationType");
            }
            token = tokenBank.nextToken();
            if (checkTokenIsPlainText(token)) {
                token.setTokenType("attributeName");
            }

        }

        void dropLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            switch (token.getName()) {
                case "DATABASE":
                    token.setTokenType("databaseSelector");
                    token = tokenBank.nextToken();
                    if (checkTokenIsPlainText(token)) {
                        token.setTokenType("databaseName");
                    }
                    break;
                case "TABLE":
                    token.setTokenType("tableSelector");
                    token = tokenBank.nextToken();
                    if (checkTokenIsPlainText(token)) {
                        token.setTokenType("tableName");
                    }
                    break;
            }
        }

        void createLexer () {
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            switch (token.getName()) {
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
            if (checkTokenIsPlainText(token)) {
                token.setTokenType("databaseName");
            }
        }

        void createTable () {
            Token token = tokenBank.getCurrentToken();
            if (checkTokenIsPlainText(token)) {
                token.setTokenType("tableName");
            }
            if (tokenBank.tokens.size() > 4) {
                token = tokenBank.nextToken();
                parenthesesLexer();
                setNullTokensInParentheses("attributeName");
            }
        }

        // parentheses is passed in
        void parenthesesLexer () {
            Token token = tokenBank.getCurrentToken();
            int initialTokenPosition = tokenBank.getCurrentTokenPosition();
            if (token.getName().equals("(")) token.setTokenType("openParentheses");
            // token = tokenBank.nextToken();
            for (int i = tokenBank.getCurrentTokenPosition(); i < tokenBank.getLastTokenPosition(); i++) {
                if (token.getName().equals(",")) {
                    token.setTokenType("comma");
                } else if ((i == tokenBank.getLastTokenPosition() - 1) && (token.getName().equals(")"))) {
                    token.setTokenType("closeParentheses");
                }
                token = tokenBank.nextToken();
            }
            tokenBank.setCurrentTokenToPosition(initialTokenPosition);
        }

        void setNullTokensInParentheses (String description){
            Token token = tokenBank.getCurrentToken();
            while (token.getTokenType() != "closeParentheses") {
                token = tokenBank.nextToken();
                if (description.equals("valueList")) {
                    addTokenIfValue();
                } else if (checkTokenIsPlainText(token)) {
                    token.setTokenType(description);
                }
            }
        }

        void addTokenIfValue () {
            Token token = tokenBank.getCurrentToken();
            if (checkTokenForPattern(token, stringLiteral)) {
                token.setTokenType("stringLiteral");
            } else if (checkTokenForPattern(token, booleanLiteral)) {
                token.setTokenType("booleanLiteral");
            } else if (checkTokenForPattern(token, floatLiteral)) {
                token.setTokenType("floatLiteral");
            } else if (checkTokenForPattern(token, integerLiteral)) {
                token.setTokenType("integerLiteral");
            } else if (token.getName().equals("NULL")) {
                token.setTokenType("NULL");
            }
        }


        void useLexer () {
            Token token = tokenBank.getCurrentToken();
            if (token.getName().equals("DATABASE")) {
                token.setTokenType("databaseSelector");
            }
            token = tokenBank.nextToken();
            if (checkTokenIsPlainText(token)) {
                token.setTokenType("databaseName");
            }
        }

        void addPlainTextToken (String description){
            Token token = tokenBank.getCurrentToken();
            if (checkTokenIsPlainText(token)) {
                token.setTokenType(description);
            }
        }

        void addKeywordTokenIfEquals (String description, String pattern){
            Token token = tokenBank.getCurrentToken();
            token.nameToUpperCase();
            if (token.getName().equals(pattern)) {
                token.setTokenType(description);
            }
        }

        boolean checkTokenIsPlainText (Token token){
            return checkTokenForPattern(token, plainText);
        }

        boolean checkTokenIsDatabaseOrTable (Token token){
            return (token.getName().equals("DATABASE") || token.getName().equals("TABLE"));
        }

        boolean checkIfAlterationType (Token token){
            return (token.getName().equals("DROP") || token.getName().equals("ADD"));
        }

        boolean checkTokenForPattern (Token token, String patternString){
            String name = token.getName();
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(name);
            return matcher.find();
        }

        void stripParenthesesToEnd () {
            Token token = tokenBank.getCurrentToken();
            int startPosition = tokenBank.getCurrentTokenPosition();
            int i = startPosition;
            while (i <= tokenBank.getLastTokenPosition()) {
                token = tokenBank.getTokenAtPosition(i);
                if (checkTokenForPattern(token, parentheses)) {
                    tokenBank.deleteToken(token.getPosition());
                } else {
                    i++;
                }
            }
            tokenBank.setCurrentTokenToPosition(startPosition);
        }


        void setup () {
            query = "CREATE DATABASE markbook;";
            tokenisedQuery = new String[]{"CREATE", "DATABASE", "markbook", ";"};

            // optional whitespace in these
            whiteSpaceSymbols = new String[]{"Command", "CommandType", "Use", "Create", "CreateDatabase", "CreateTable", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join", "NameValueList", "NameValuePair", "AlterationType", "ValueList", "WildAttribList", "AttributeList", "Condition", "FirstCondition", "SecondCondition", "BoolOperator", "Comparator"};

            // no additional whitespace
            nonWhiteSpaceSymbols = new String[]{"Digit", "Uppercase", "Lowercase", "Letter", "PlainText", "Symbol", "Space", "DigitSequence", "IntegerLiteral", "FloatLiteral", "BooleanLiteral", "CharLiteral", "StringLiteral", "Value", "TableName", "AttributeName", "DatabaseName"};

            commandArray = new String[]{"CommandType", ";"};

            commandTypeArray = new String[]{"Use", "Create", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join"};
            commandType = String.join("|", commandTypeArray).toUpperCase();

            tableOrDatabase = "TABLE|DATABASE";

            alterationType = "ADD|DROP";

            plainText = "\\p{Alnum}+";

            symbolArray = new String[]{"!", "#", "$", "%", "&", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", ">", "=", "<", "?", "@", "\\[", "\\\\", "\\]", "^", "_", "`", "{", "}", "~"};
            symbol = String.join("", symbolArray);

            stringLiteral = "^'[\\s\\p{Alnum}" + symbol + "]*'$";

            booleanLiteral = "TRUE|FALSE";

            booleanOperator = "AND|OR";

            floatLiteral = "^[+-]?\\p{Digit}+\\.\\p{Digit}+$";

            integerLiteral = "^[+-]?\\p{Digit}+$";

            wildAttributeList = plainText + "|\\*";

            comparatorArray = new String[]{"==", ">", "<", ">=", "<=", "!=", "LIKE"};
            comparator = String.join("|", comparatorArray);

            parentheses = "\\(|\\)";
        }
}

