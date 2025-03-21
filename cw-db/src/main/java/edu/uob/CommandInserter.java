package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CommandInserter implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = currentDatabase.tableMap.get(tableName);
        int newID = currentDatabase.getNewID();

        List<Token> valueList = tokenBank.getTokenTypeFromFragment("valueList", "openParenthesis", "closeParenthesis");
        table.addRow(valueList, newID);
        return "[OK]";
    }
}
