package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CommandUpdater implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = currentDatabase.getTable(tableName);

        List<Token> nameValuePairs = tokenBank.getTokenTypeFromFragment("nameValuePairs", "set", "where");
        List<Token> conditionFilter = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");

        table.updateFilteredRows(nameValuePairs, conditionFilter);
        table.filterTable(null, null);
        return "[OK]";
    }
}
