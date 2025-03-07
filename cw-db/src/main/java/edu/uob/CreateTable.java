package edu.uob;

public class CreateTable extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
        if (tokenBank.getNextToken().equals(";")) {
//            return "CREATE DATABASE " + tokenBank.getCurrentToken();
            return this;
        }
        else if (tokenBank.getNextToken().equals("(")) {
            DBCommand createTableWithAttributes = new createTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);

        }
        return null;
    }
}