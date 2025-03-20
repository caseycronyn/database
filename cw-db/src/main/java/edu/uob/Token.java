package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    private String value, tokenType;
    private Integer position;
    String[] whiteSpaceSymbols, nonWhiteSpaceSymbols, commandArray, commandTypeArray, symbolArray, comparatorArray;
    String commandType, plainText, tableOrDatabase, alterationType, stringLiteral, booleanLiteral, floatLiteral, integerLiteral, symbol, wildAttributeList, comparator, parentheses, booleanOperator;

    Token (String name, Integer position) {
        this.value = name;
        this.position = position;
        tokenType = null;
        createStrings();
        // dataType = null;
    }

    Token copy() {
        Token token = new Token(value, position);
        token.setTokenType(tokenType);
        return token;
    }

    boolean isNameValuePair () {
        return getTokenType().equals("attributeName") || getTokenType().equals("equals") || isAValue();
    }


    boolean checkForPattern(String patternString){
        String name = getValue();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    boolean isComparator() {
        return checkForPattern(comparator);
    }

    boolean isBooleanLiteral() {
        return checkForPattern(booleanLiteral);
    }

    boolean isBooleanOperator() {
        return checkForPattern(booleanOperator);
    }

    boolean isWildAttributeList() {
        return checkForPattern(wildAttributeList);
    }

    void setValueTokenType() {
        if (whiteSpaceSymbols == null) {
            createStrings();
        }
        // Token token = tokenBank.getCurrentToken();
        if (checkForPattern(booleanLiteral)) {
            setTokenType("booleanLiteral");
        }
        else if (checkForPattern(floatLiteral)) {
            setTokenType("floatLiteral");
        }
        else if (checkForPattern(integerLiteral)) {
            setTokenType("integerLiteral");
        }
        else if (getValue().equals("NULL")) {
            setTokenType("NULL");
        }
        else if (checkForPattern(stringLiteral)) {
            removeStringQuotationMarks();
            setTokenType("stringLiteral");
        }
    }

    void removeStringQuotationMarks() {
        setValue(getValue().replaceAll("'", ""));
    }

    boolean isCommandType() {
        return checkForPattern(commandType);
    }

    boolean isPlainText (){
        return checkForPattern(plainText);
    }

    boolean isACondition() {
        return (getTokenType().equals("attributeName") || getTokenType().equals("comparator") || isAValue());
    }


    public boolean isAValue() {
        return (getTokenType().contains("Literal") || getTokenType().equals("NULL"));
    }

    void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    String getTokenType() {
        return tokenType;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void nameToUpperCase() {
        value = value.toUpperCase();
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    int getIntegerValue() {
        return Integer.parseInt(value);
    }

    float getFloatValue() {
        return Float.parseFloat(value);
    }

    boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
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

        plainText = "^\\p{Alnum}+$";

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
