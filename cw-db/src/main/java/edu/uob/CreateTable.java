package edu.uob;

import java.io.IOException;

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
        try {
            table.writeEmptyTableToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.databases.get(databaseName).tables.put(tableName, table);
        return "[OK]";
    }
}