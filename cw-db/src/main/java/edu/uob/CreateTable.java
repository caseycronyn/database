package edu.uob;

import java.io.IOException;

public class CreateTable implements DBCommand{
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Database database = databaseManager.getCurrentDatabase();
        Table table = new Table(tableName, database.getName(), databaseManager.getStorageFolderPath());
        try {
            table.writeEmptyTableToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        database.tables.put(tableName, table);
        return "[OK]";
    }
}