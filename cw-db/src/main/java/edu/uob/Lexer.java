package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// will decide what kind of token each string is
public class Lexer {
    String [] whiteSpaceSymbols, nonWhiteSpaceSymbols, commandArray, commandTypeArray, tokenisedQuery;
    String commandType, plainText, query;
    TokenBank tokenBank;

    // public static void main(String[] args) {
    //     // Lexer lexer = new Lexer();
    // }

    Lexer(TokenBank tokenBank) {
        setup();
        this.tokenBank = tokenBank;
        // System.out.println(tokenBank.getTokens());
        // setup();
        // check for command
        lexTokens();
        // convert commands tokenBank uppercase
        // check all tokens are set
        // for (String token : tokenisedQuery) {
        //     System.out.println(checkForCommand(command, token));
        // }
        tokenBank.printTokenNames();
        tokenBank.printTokenTypes();
    }

    // void setup() {
    //     // String.join("|", commandType);
    // }

    // should return a hashmap of tokens to set in tokenbank or an error if something is wrong. sets token types
    void lexTokens() {
        semicolonLexer();
        commandTypeLexer();
    }

    void semicolonLexer() {
        if (tokenBank.getLastToken().getName().equals(";")) {
            tokenBank.getLastToken().tokenType = "terminator";
        }
    }

    void commandTypeLexer() {
        Token token = tokenBank.getFirstToken();
        if (checkTokenForPattern(token, commandType)) {
            tokenBank.getFirstToken().tokenType = "command";
            // next one
            switch (token.getName().toUpperCase()) {
                case "CREATE":
                    createLexer();
                    break;
                case "USE":
                    useLexer();
                    break;
            }
        }
        // System.out.println(tokenBank.getCurrentToken());
    }

    void createLexer() {
        Token token = tokenBank.getTokenAtPosition(1);
        switch (token.getName().toUpperCase()) {
            case "DATABASE":
                token.setTokenType("createDatabase");
                createDatabaseLexer();
                break;
        }
    }

    void createDatabaseLexer() {
        Token token = tokenBank.getTokenAtPosition(2);
        if (checkTokenIsPlainText(token)) {
            token.tokenType = "databaseName";
        }
    }

    void createTable() {
        if (tokenBank.getLastToken().getTokenType().equals("terminal")) {
            tokenBank.getTokenAtPosition(2).tokenType = "tableName";
        }
        else {
            getArgumentsFromParentheses(3);
            createTableWithAttributes();
        }
    }

    void createTableWithAttributes() {

    }

    void getArgumentsFromParentheses() {

    }

    void useLexer() {
        Token token = tokenBank.getTokenAtPosition(1);
        if (checkTokenIsPlainText(token)) {
            token.setTokenType("databaseName");
        }
    }

    boolean checkTokenIsPlainText(Token token) {
        String plainText = "\\p{Alnum}+";
        return checkTokenForPattern(token, plainText);
    }

    boolean checkTokenForPattern(Token token, String patternString) {
        String name = token.getName();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
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

        commandTypeArray =  new String[]{"Use", "Create", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join"};
        commandType = String.join("|", commandTypeArray);


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

}
