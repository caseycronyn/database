package edu.uob;

public class CreateTable extends Parser {
    String tableName;
    String databaseName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
        if (tokenBank.getNextToken().equals(";")) {
            tableName = tokenBank.getCurrentToken();
            return this;
        }
        else if (tokenBank.getNextToken().equals("(")) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(tokenBankLocal.getCurrentToken());
        databaseName = server.getCurrentDatabase();
        server.createNewTable(tableName, databaseName);
//        System.out.println(server.getCurrentDatabase());
    };
}