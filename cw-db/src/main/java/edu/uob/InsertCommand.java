package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class InsertCommand extends Parser {
    List<Token> valueList;
    String tableName;
    Table table;
    
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
        tableName = tokenBank.getCurrentToken().getName();
        tokenBank.nextToken();
        tokenBank.nextToken();
        valueList = getAttributesFromParenthesis(tokenBank);
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
//        System.out.println(table.attributes);
//         table.addEntryToTable(valueList, server.databases.get(server.getCurrentDatabase()).getAndIncrementID());
        table.writeTableToFileFromMemory();
    };
}
