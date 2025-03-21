package edu.uob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    private String value, tokenType;
    private final Integer position;
    String[] commandTypeArray, symbolArray, comparatorArray, whiteSpaceSymbols;
    String commandType, plainText, tableOrDatabase, alterationType, stringLiteral, booleanLiteral, floatLiteral, integerLiteral, symbol, wildAttributeList, comparator, parentheses, booleanOperator;

    Token(String name, Integer position) {
        this.value = name;
        this.position = position;
        tokenType = null;
        createStrings();
    }

    Token copyToken() {
        Token tokenCopy = new Token(value, position);
        tokenCopy.setTokenType(tokenType);
        return tokenCopy;
    }

    boolean isValuePair() {
        return getTokenType().equals("attributeName") || getTokenType().equals("equals") || isAValue();
    }


    boolean checkPattern(String patternString){
        String value = getValue();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    boolean isComparator() {
        return checkPattern(comparator);
    }

    boolean isBooleanOperator() {
        return checkPattern(booleanOperator);
    }

    boolean isWildAttributeList() {
        return checkPattern(wildAttributeList);
    }

    void setValueType() {
        if (whiteSpaceSymbols == null) {
            createStrings();
        }
        if (checkPattern(booleanLiteral)) {
            setTokenType("booleanLiteral");
        }
        else if (checkPattern(floatLiteral)) {
            setTokenType("floatLiteral");
        }
        else if (checkPattern(integerLiteral)) {
            setTokenType("integerLiteral");
        }
        else if (getValue().equals("NULL")) {
            setTokenType("NULL");
        }
        else if (checkPattern(stringLiteral)) {
            removeQuotes();
            setTokenType("stringLiteral");
        }
    }

    void removeQuotes() {
        setValue(getValue().replaceAll("'", ""));
    }

    boolean isCommandType() {
        return checkPattern(commandType);
    }

    boolean isPlainText (){
        return checkPattern(plainText);
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

    void nameToUpper() {
        value = value.toUpperCase();
    }

    int getPosition() {
        return position;
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

        whiteSpaceSymbols = new String[]{"Command", "CommandType", "Use", "Create", "CreateDatabase", "CreateTable", "Drop", "Alter", "Insert", "Select", "Update", "Delete", "Join", "NameValueList", "NameValuePair", "AlterationType", "ValueList", "WildAttribList", "AttributeList", "Condition", "FirstCondition", "SecondCondition", "BoolOperator", "Comparator"};
    }
}
