package edu.uob;

public class CreateTable implements DBCommand{
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String databaseName = server.getCurrentDatabase();
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.writeEmptyTableToFile();
        server.databases.get(databaseName).tables.put(tableName, table);
        return "[OK]";
    }
}