package edu.uob;

import java.util.List;

public class InsertCommand extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        List<Token> valueList = tokenBank.getTokenTypeFromFragment("valueList", "openParenthesis", "closeParenthesis");
        int newID = server.databases.get(server.getCurrentDatabase()).getAndIncrementID();
        table.addRowToTable(valueList, newID);
    };
}
