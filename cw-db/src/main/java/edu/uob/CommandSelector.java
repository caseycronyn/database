package edu.uob;

import java.util.List;

public class CommandSelector implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = currentDatabase.tableMap.get(tableName);
        if (table == null) {
            return "[ERROR]: Table " + tableName + " not found";
        }
        List<Token> conditionFilter = null;
        if (tokenBank.tokenExistsInQuery("where")) {
            conditionFilter = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        }

        List<Token> attributeFilter = null;
        boolean isWildAttribute = tokenBank.tokenValueExists("*");
        if (!isWildAttribute) {
            attributeFilter = tokenBank.getTokenTypeFromFragment("attributeName", "selectCommand", "from");
            if (!table.attributesExist(attributeFilter)) {
                return "[ERROR]: attributes not found";
            }
        }
        if (!currentDatabase.tableExists(tableName)) {
            return "[ERROR]: Table " + tableName + " not found";
        }
        return "[OK]\n" + table.filterTableWithAttributesAndCondition(attributeFilter, conditionFilter);
    }
}
