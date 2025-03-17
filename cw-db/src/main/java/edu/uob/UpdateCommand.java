package edu.uob;

import java.util.List;

public class UpdateCommand implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        List<Token> nameVauePairs = tokenBank.getTokenTypeFromFragment("nameValuePairs", "set", "where");
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        table.changeValuesInTableWhereCondition(nameVauePairs, condition);
        table.filterTableWithAttributesAndCondition(null, null);
    }
}
