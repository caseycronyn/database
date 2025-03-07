package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTableWithAttributes extends Parser {
    String tableName;
    String databaseName;
    ArrayList<String> attributes;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tableName = tokenBank.getCurrentToken();
        tokenBank.nextToken();
        Attributes attributesMaker = new Attributes();
        attributes = attributesMaker.buildAttributes(tokenBank);
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
        databaseName = server.getCurrentDatabase();
//        System.out.println(tableName);
        server.createNewTable(tableName, databaseName);
    }
}
