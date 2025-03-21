package edu.uob;

import java.util.List;

public class CommandSelector implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        Database database = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = database.tables.get(tableName);
        if (table == null) {
            return "[ERROR]: Table " + tableName + " not found";
        }
        List<Token> condition = null;
        if (tokenBank.tokenExistsInQuery("where")) {
            condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        }

        List<Token> attributeNames = null;
        boolean wildAttribute = tokenBank.tokenValueExists("*");
        if (!wildAttribute) {
            attributeNames = tokenBank.getTokenTypeFromFragment("attributeName", "selectCommand", "from");
            if (!table.attributesExist(attributeNames)) {
                return "[ERROR]: attributes not found";
            }
        }
        if (!database.tableExists(tableName)) {
            return "[ERROR]: Table " + tableName + " not found";
        }
        return "[OK]\n" + table.filterTableWithAttributesAndCondition(attributeNames, condition);
    }
}
