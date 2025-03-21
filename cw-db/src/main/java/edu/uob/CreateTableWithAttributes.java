package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CreateTableWithAttributes implements DBCommand{
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Database database = databaseManager.getCurrentDatabase();
        List<Token> attributes = tokenBank.getTokenTypeFromFragment("attributeName", "openParenthesis", "closeParenthesis");
        Table table = new Table(tableName, database.getName(), databaseManager.getStorageFolderPath());
        table.addAttributesToTable(attributes);
        database.tables.put(tableName, table);
        return "[OK]";
    }
}
