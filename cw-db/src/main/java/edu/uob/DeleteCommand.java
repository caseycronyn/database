package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class DeleteCommand implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = databaseManager.getCurrentDatabase().tables.get(tableName);
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        table.deleteFromTableOnCondition(condition);
        return "[OK]";
    }
}
