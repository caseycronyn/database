package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CommandInserter implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = dbManager.getCurrentDatabase().tableMap.get(tableName);
        List<Token> valueList = tokenBank.getTokenTypeFromFragment("valueList", "openParenthesis", "closeParenthesis");
        int newID = dbManager.getCurrentDatabase().getNewID();
        table.addRowToTable(valueList, newID);
        return "[OK]";
    }
}
