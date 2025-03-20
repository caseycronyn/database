package edu.uob;

import java.util.List;

public class SelectAllCommand implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        Database database = server.databases.get(server.getCurrentDatabase());
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = database.tables.get(tableName);
        if (table == null) {
            return "[ERROR]: Table " + tableName + " not found";
        }
        List<Token> condition = null;
        if (tokenBank.tokenExistsInQuery("where")) {
            condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        }
        if (database.tableExists(tableName)) {
            return "[OK]\n" + table.filterTableWithAttributesAndCondition(null, condition);
        }
        else {
            return "[ERROR]";
        }
    }
}
