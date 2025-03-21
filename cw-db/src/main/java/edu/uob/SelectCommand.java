package edu.uob;

import java.util.List;

public class SelectCommand implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager dbManager, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = dbManager.getCurrentDatabase().tables.get(tableName);
        if (table == null) {
            return "[ERROR]: Table " + tableName + " not found";
        }

        List<Token> attributeNames = tokenBank.getTokenTypeFromFragment("attributeName", "selectCommand", "from");
        if (!table.attributesExist(attributeNames)) {
            return "[ERROR]: attributes not found";
        }
        List<Token> condition = null;
        if (tokenBank.tokenExistsInQuery("where")) {
            condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        }
        return "[OK]\n" + table.filterTableWithAttributesAndCondition(attributeNames, condition);
    }
}
