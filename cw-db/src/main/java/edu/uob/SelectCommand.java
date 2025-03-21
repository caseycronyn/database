package edu.uob;

import java.util.List;

public class SelectCommand implements DBCommand {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
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
