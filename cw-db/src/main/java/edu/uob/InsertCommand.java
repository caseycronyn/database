package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class InsertCommand extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        // tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
//         tableName = tokenBank.getCurrentToken().getName();
//         tokenBank.nextToken();
//         tokenBank.nextToken();
//         valueList = getAttributesFromParenthesis(tokenBank);
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        List<Token> valueList = getTokenTypeFromParentheses(tokenBank, "valueList");

        int newID = server.databases.get(server.getCurrentDatabase()).getAndIncrementID();
        table.addRowToTable(valueList, newID);

        // table.addEntryToTable(valueList, server.databases.get(server.getCurrentDatabase()).getAndIncrementID());
        table.writeTableToFileFromMemory();
    };
}
