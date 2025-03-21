package edu.uob;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TableCreator implements DBCommand{
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = DBManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = new Table(tableName, currentDatabase.getName(), DBManager.getStorageFolderPath());

        boolean withAttributes = tokenBank.tokenValueExists("(");
        if (withAttributes) {
            List<Token> attributes = tokenBank.getTokenTypeFromFragment("attributeName", "openParenthesis", "closeParenthesis");
            table.addAttributeList(attributes);
        }
        try {
            table.writeEmptyFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentDatabase.tableMap.put(tableName, table);
        return "[OK]";
    }
}