package edu.uob;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TableCreator implements DBCommand{
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Database database = DBManager.getCurrentDatabase();
        Table table = new Table(tableName, database.getName(), DBManager.getStorageFolderPath());
        boolean withAttributes = tokenBank.tokenValueExists("(");
        if (withAttributes) {
            List<Token> attributes = tokenBank.getTokenTypeFromFragment("attributeName", "openParenthesis", "closeParenthesis");
            table.addAttributesToTable(attributes);
        }
        try {
            table.writeEmptyTableToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        database.tables.put(tableName, table);
        return "[OK]";
    }
}