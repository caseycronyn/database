package edu.uob;

import java.util.List;

public class DeleteCommand implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        table.deleteFromTableOnCondition(condition);
        // System.out.println("Deleted " + tableName);
    }
}
