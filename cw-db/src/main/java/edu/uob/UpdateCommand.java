package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class UpdateCommand implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager dbManager, TokenBank tokenBank) throws FileNotFoundException {
        List<Token> nameValuePairs = tokenBank.getTokenTypeFromFragment("nameValuePairs", "set", "where");
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = dbManager.getCurrentDatabase().tables.get(tableName);
        table.changeValuesInTableWhereCondition(nameValuePairs, condition);
        table.filterTableWithAttributesAndCondition(null, null);
        return "[OK]";
    }
}
