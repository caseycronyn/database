package edu.uob;

import java.util.List;

public class CreateTableWithAttributes extends Parser {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getNameFromType("tableName");
        String databaseName = server.getCurrentDatabase();
        List<Token> attributes = getAttributesFromParenthesis(tokenBank);
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.setAttributes(attributes);
        table.writeTableToFileFromMemory();
        server.databases.get(databaseName).tables.put(tableName, table);
    }
}
