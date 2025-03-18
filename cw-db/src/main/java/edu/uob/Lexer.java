package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// will decide what kind of token each string is
public class Lexer {
    String[] whiteSpaceSymbols, nonWhiteSpaceSymbols, commandArray, commandTypeArray, tokenisedQuery, symbolArray, comparatorArray;
    String commandType, plainText, query, tableOrDatabase, alterationType, stringLiteral, booleanLiteral, floatLiteral, integerLiteral, symbol, wildAttributeList, comparator, parentheses, booleanOperator;
    TokenBank tokenBank;

    void checkForNullTokens() throws Exception {
        for (Token token : tokenBank.getTokens()) {
            if (token.getTokenType() == null) {
                System.out.print("token types: ");
                tokenBank.printTokenTypes();
                tokenBank.printTokenNames();
                throw new Exception("error has occurred during lexing of tokens. Some are null");
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
        if (checkTokenForPattern(token, commandType)) {
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
        // tokenBank.printTokenNames();
        // tokenBank.printTokenTypes();

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
        setValueTokenType(token);
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
                    if (checkTokenForPattern(token, booleanOperator)) {
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
            tokenBank.nextToken();
            addTokenIfPatternCheckSuccessful("comparator", comparator);
            Token token = tokenBank.nextToken();
            setValueTokenType(token);
        }

        void addTokenIfPatternCheckSuccessful (String description, String pattern){
            Token token = tokenBank.getCurrentToken();
            if (checkTokenForPattern(token, pattern)) {
                token.setTokenType(description);
            }
        }

        void getAttributeNames() {
            Token token = tokenBank.getCurrentToken();
            while (!token.getValue().equalsIgnoreCase("FROM")) {
                if (checkTokenForPattern(token, wildAttributeList)) {
                    if (token.getValue().equals("*")) {
                        token.setTokenType("wildAttributeSymbol");
                    } else token.setTokenType("attributeName");
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
            switch (token.getValue()) {
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
        // lexes all parentheses
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
                throw new java.lang.Error("error: help! no closing parentheses");
            }
            tokenBank.setCurrentTokenToPosition(initialTokenPosition);
        }

        void setNullTokensInParentheses (String description){
            Token token = tokenBank.getCurrentToken();
            while (!token.getTokenType().equals("closeParenthesis")) {
                token = tokenBank.nextToken();
                if (description.equals("valueList")) {
                    setValueTokenType(token);
                } else if (checkTokenIsPlainText(token)) {
                    token.setTokenType(description);
                }
            }
        }

        void setValueTokenType(Token token) {
            // Token token = tokenBank.getCurrentToken();
            if (checkTokenForPattern(token, booleanLiteral)) {
                token.setTokenType("booleanLiteral");
            }
            else if (checkTokenForPattern(token, floatLiteral)) {
                token.setTokenType("floatLiteral");
            }
            else if (checkTokenForPattern(token, integerLiteral)) {
                token.setTokenType("integerLiteral");
            }
            else if (token.getValue().equals("NULL")) {
                token.setTokenType("NULL");
            }
            else if (checkTokenForPattern(token, stringLiteral)) {
                removeStringQuotationMarks(token);
                token.setTokenType("stringLiteral");
            }
        }

        void removeStringQuotationMarks(Token token) {
            token.setValue(token.getValue().replaceAll("'", ""));
        }

        void useLexer () {
            Token token = tokenBank.getCurrentToken();
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
            if (token.getValue().equals(pattern)) {
                token.setTokenType(description);
            }
        }

        boolean checkTokenIsPlainText (Token token){
            return checkTokenForPattern(token, plainText);
        }

        boolean checkTokenIsDatabaseOrTable (Token token){
            return (token.getValue().equals("DATABASE") || token.getValue().equals("TABLE"));
        }

        boolean checkIfAlterationType (Token token){
            return (token.getValue().equals("DROP") || token.getValue().equals("ADD"));
        }

        boolean checkTokenForPattern (Token token, String patternString){
            String name = token.getValue();
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
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

        void createStrings() {
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

        void setup (TokenBank tokenBank) throws Exception {
            this.tokenBank = tokenBank;
            createStrings();
            lexTokens();
            tokenBank.initialise();
            checkForNullTokens();
        }
}

