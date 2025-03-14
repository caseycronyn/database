package edu.uob;

public class CreateTable extends Parser {
    TokenBank tokenBank;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
/*        tokenBank.nextToken();
        if (tokenBank.nextToken().equals(";")) {
            tableName = tokenBank.getCurrentToken().getName();
            return this;
        }
        else if (tokenBank.nextToken().equals("(")) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        return null;*/
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getNameFromType("tableName");
        String databaseName = server.getCurrentDatabase();
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.writeEmptyTableToFile();
        server.databases.get(databaseName).tables.put(tableName, table);
    };
}