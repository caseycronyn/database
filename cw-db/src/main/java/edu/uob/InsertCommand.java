package edu.uob;

import java.util.ArrayList;

public class InsertCommand extends Parser {
    ArrayList<String> valueList;
    String tableName;
    Table table;
    
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
        tableName = tokenBank.getCurrentToken();
        tokenBank.nextToken();
        tokenBank.nextToken();
        Arguments valueListBuilder = new Arguments();
        valueList = valueListBuilder.buildArguments(tokenBank);
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
        table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
//        System.out.println(table.attributes);
        table.addEntryToTable(valueList, server.databases.get(server.getCurrentDatabase()).getAndIncrementID());
        table.writeTableToFileFromMemory();
    };
}
