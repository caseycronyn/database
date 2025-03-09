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
        Table table = new Table(tableName, server.getCurrentDatabase(), server.getStorageFolderPath());
        table.setAttributes(attributes);
        table.writeTableToFileFromMemory();
        server.databases.get(databaseName).tables.put(tableName, table);
    }
}
