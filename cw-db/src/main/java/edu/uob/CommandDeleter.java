package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CommandDeleter implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table currentTable = currentDatabase.getTable(tableName);

        List<Token> conditionFilter = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        currentTable.deleteOnFilter(conditionFilter);
        return "[OK]";
    }
}
