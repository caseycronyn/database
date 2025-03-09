package edu.uob;

import java.io.File;

public class CreateTable extends Parser {
    String tableName;
    String databaseName;
    String storageFolderPath;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
        if (tokenBank.getNextToken().equals(";")) {
            tableName = tokenBank.getCurrentToken();
            return this;
        }
        else if (tokenBank.getNextToken().equals("(")) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(tokenBankLocal.getCurrentToken());
        String databaseName = server.getCurrentDatabase();
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.writeEmptyTableToFile();
        server.databases.get(databaseName).tables.put(tableName, table);

//        System.out.println(server.getCurrentDatabase());
    };
}