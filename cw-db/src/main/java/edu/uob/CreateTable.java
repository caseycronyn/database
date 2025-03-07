package edu.uob;

public class CreateTable extends Parser {
    TokenBank tokenBankLocal = new TokenBank();

    @Override
    public DBCommand parse(TokenBank tokenBankIn) {
        tokenBankLocal = tokenBankIn;
        tokenBankIn.nextToken();
        if (tokenBankIn.getNextToken().equals(";")) {
//            return "CREATE DATABASE " + tokenBank.getCurrentToken();
            return this;
        }
        else if (tokenBankIn.getNextToken().equals("(")) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBankIn);
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(tokenBankLocal.getCurrentToken());
        server.createNewTable(tokenBankLocal.getCurrentToken());
    };
}