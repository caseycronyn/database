package edu.uob;

import java.util.ArrayList;

public class InsertCommand extends Parser {
    ArrayList<String> valueList;
    String tableName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
        tableName = tokenBank.getCurrentToken();
        tokenBank.nextToken();
        tokenBank.nextToken();
        ArgumentsWithinParentheses valueListBuilder = new ArgumentsWithinParentheses();
        valueList = valueListBuilder.buildArguments(tokenBank);
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        server.databases.get(server.getCurrentDatabase()).tables.get(tableName).;
    };
}
