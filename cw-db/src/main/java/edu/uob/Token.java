package edu.uob;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// will decide what kind of token each string is
public class Token {
    String [] whiteSpaceSymbols;

    public static void main(String[] args) {
        Token token = new Token();
    }

    Token() {
        setupTests();
        setupArrays();
        categoriseTokens();
    }

    void setupTests() {
    }

    void setupArrays() {
        // optional whitespace in these
        String[] whiteSpaceSymbols = {"Command", "CommandType", "Use", "Create", "CreateDatabase", "CreateTable", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join", "NameValueList", "NameValuePair", "AlterationType", "ValueList", "WildAttribList", "AttributeList", "Condition", "FirstCondition", "SecondCondition", "BoolOperator", "Comparator"};

        // no additional whitespace
        String[] nonWhiteSpaceSymbols = {"Digit", "Uppercase", "Lowercase", "Letter", "PlainText", "Symbol", "Space", "DigitSequence", "IntegerLiteral", "FloatLiteral", "BooleanLiteral", "CharLiteral", "StringLiteral", "Value", "TableName", "AttributeName", "DatabaseName"};

        String [] command = {"CommandType", ";"};

        String [] commandType =  {"Use", "Create", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join"};

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

    void categoriseTokens() {
    }

}
