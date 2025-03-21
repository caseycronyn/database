package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CommandDeleter implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = DBManager.getCurrentDatabase().tables.get(tableName);
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        table.deleteFromTableOnCondition(condition);
        return "[OK]";
    }
}
