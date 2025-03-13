package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// will decide what kind of token each string is
public class Lexer {
    String [] whiteSpaceSymbols, nonWhiteSpaceSymbols, commandArray, commandTypeArray, tokenisedQuery, symbolArray;
    String commandType, plainText, query, tableOrDatabase, alterationType, stringLiteral, booleanLiteral, floatLiteral, integerLiteral, symbol;
    TokenBank tokenBank;

    // public static void main(String[] args) {
    //     // Lexer lexer = new Lexer();
    // }

    Lexer(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
        // System.out.println(tokenBank.getTokens());
        // check for command
        setup();
        lexTokens();
        checkForNullTokens();
        tokenBank.printTokenTypes();
        // check to make sure the order is good?
        // convert commands tokenBank uppercase
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
            token.setTokenType("commandType");
            // next one
            switch (token.getName()) {
                case "CREATE":
                    createLexer();
                    break;
                case "USE":
                    useLexer();
                    break;
                case "DROP":
                    dropLexer();
                    break;
                case "ALTER":
                    alterLexer();
                    break;
                case "INSERT":
                    insertLexer();
                    break;
                case "SELECT":
                    selectLexer();
                    break;
            }
        }
    }

    void selectLexer() {

    }
    
    void insertLexer() {
        addKeywordTokenIfEquals("into", "INTO");
        addPlainTextToken("tableName");
        addKeywordTokenIfEquals("values", "VALUES");
        tokenBank.incrementCurrentTokenPosition();
        parenthesesLexer(tokenBank.getCurrentTokenPosition(), "valueList");
    }

    void alterLexer() {
        Token token = tokenBank.nextToken();
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

    void dropLexer() {
        Token token = tokenBank.nextToken();
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

    void createLexer() {
        Token token = tokenBank.nextToken();
        token.nameToUpperCase();
        switch (token.getName()) {
            case "DATABASE":
                token.setTokenType("databaseSelector");
                createDatabaseLexer();
                break;
            case "TABLE":
                token.setTokenType("tableSelector");
                createTable();
                break;
        }
    }

    void createDatabaseLexer() {
        Token token = tokenBank.nextToken();
        if (checkTokenIsPlainText(token)) {
            token.setTokenType("databaseName");
        }
    }

    void createTable() {
        Token token = tokenBank.nextToken();
        if (checkTokenIsPlainText(token)) {
            token.setTokenType("tableName");
        }
        if (tokenBank.tokens.size() > 4) {
            int startPosition = 3;
            parenthesesLexer(startPosition, "attributeName");
        }
    }

    // parentheses is passed in
    void parenthesesLexer(int tokenPosition, String description) {
        Token token = tokenBank.getTokenAtPosition(tokenPosition);
        if (token.getName().equals("(")) token.setTokenType("openParentheses");
        tokenPosition++;
        for ( ; tokenPosition <= tokenBank.getLastTokenPosition(); tokenPosition++) {
            token = tokenBank.getTokenAtPosition(tokenPosition);
            if (token.getName().equals(",")) {
                token.setTokenType("comma");
            }
            else if ((tokenPosition == tokenBank.getLastTokenPosition()) && (token.getName().equals(")"))) {
                token.setTokenType("closeParentheses");
            }
            else if (checkTokenIsPlainText(token)) {
                setTokenInParentheses(token, description);
            }
        }
    }

    void setTokenInParentheses(Token token, String description) {
        if (description.equals("attributeName")) {
            token.setTokenType("attributeName");
        }
        else if (description.equals("valueList")) {
            if (checkTokenForPattern(token, stringLiteral)) {
                token.setTokenType("stringLiteral");
            }
            else if (checkTokenForPattern(token, booleanLiteral)) {
                token.setTokenType("booleanLiteral");
            }
            else if (checkTokenForPattern(token, floatLiteral)) {
                token.setTokenType("floatLiteral");
            }
            else if (checkTokenForPattern(token, integerLiteral)) {
                token.setTokenType("integerLiteral");
            }
            else if (token.getName().equals("NULL")) {
                token.setTokenType("NULL");
            }
        }
    }


    void useLexer() {
        Token token = tokenBank.nextToken();
        if (token.getName().equals("DATABASE")) {
            token.setTokenType("databaseSelector");
        }
        token = tokenBank.nextToken();
        if (checkTokenIsPlainText(token)) {
            token.setTokenType("databaseName");
        }
    }

    void addPlainTextToken(String description) {
        Token token = tokenBank.nextToken();
        if (checkTokenIsPlainText(token)) {
            token.setTokenType(description);
        }
    }

    void addKeywordTokenIfEquals(String description, String pattern) {
        Token token = tokenBank.nextToken();
        token.nameToUpperCase();
        if (token.getName().equals(pattern)) {
            token.setTokenType(description);
        }
    }

    boolean checkTokenIsPlainText(Token token) {
        return checkTokenForPattern(token, plainText);
    }

    boolean checkTokenIsDatabaseOrTable(Token token) {
        return (token.getName().equals("DATABASE") || token.getName().equals("TABLE"));
    }

    boolean checkIfAlterationType(Token token) {
        return(token.getName().equals("DROP") || token.getName().equals("ADD"));
    }

    boolean checkTokenForPattern(Token token, String patternString) {
        String name = token.getName();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }



    void setup() {
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

        floatLiteral = "^[+-]?\\p{Digit}+\\.\\p{Digit}+$";

        integerLiteral = "^[+-]?\\p{Digit}+$";
    }

        // keywords = new String[]{
        //
        // USE
        // CREATE
        // DATABASE
        // TABLE
        // DROP
        // ALTER
        // INSERT
        // INTO
        // VALUES
        // FROM
        // WHERE
        // UPDATE
        // SET
        // DELETE
        // JOIN
        // AND
        // ON
        //
        //
        // }





        // String [] use = "USE " [DatabaseName]
        //
        // String [] create          ::=  <CreateDatabase> | <CreateTable>
        //
        // String [] createDatabase = "CREATE " "DATABASE " [DatabaseName]
        //
        // String [] createTable = "CREATE " "TABLE " [TableName] | "CREATE " "TABLE " [TableName] "(" <AttributeList> ")"
        //
        // String [] drop = "DROP " "DATABASE " [DatabaseName] | "DROP " "TABLE " [TableName]
        //
        // String [] alter = "ALTER " "TABLE " [TableName] " " <AlterationType> " " [AttributeName]
        //
        // String [] insert = "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
        //
        // String [] select = "SELECT " <WildAttribList> " FROM " [TableName] | "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
        //
        // String [] update = "UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
        //
        // String [] delete = "DELETE " "FROM " [TableName] " WHERE " <Condition>
        //
        // String [] join = "JOIN " [TableName] " AND " [TableName] " ON " [AttributeName] " AND " [AttributeName]


}
