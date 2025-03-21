package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class InsertCommand implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = dbManager.getCurrentDatabase().tables.get(tableName);
        List<Token> valueList = tokenBank.getTokenTypeFromFragment("valueList", "openParenthesis", "closeParenthesis");
        int newID = dbManager.getCurrentDatabase().getAndIncrementID();
        table.addRowToTable(valueList, newID);
        return "[OK]";
    }
}
