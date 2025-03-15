package edu.uob;

import java.util.List;

public class CreateTableWithAttributes extends Parser {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        String databaseName = server.getCurrentDatabase();
        List<Token> attributes = getTokenTypeFromParentheses(tokenBank, "attributeName");
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.addAttributesToTable(attributes);
        server.databases.get(databaseName).tables.put(tableName, table);
    }
}
