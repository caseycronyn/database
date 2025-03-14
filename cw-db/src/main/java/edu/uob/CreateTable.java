package edu.uob;

public class CreateTable extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        String databaseName = server.getCurrentDatabase();
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.writeEmptyTableToFile();
        server.databases.get(databaseName).tables.put(tableName, table);
    };
}