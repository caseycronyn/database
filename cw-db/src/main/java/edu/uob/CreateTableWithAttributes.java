package edu.uob;

import java.util.List;

public class CreateTableWithAttributes extends Parser {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String databaseName = server.getCurrentDatabase();
        List<Token> attributes = tokenBank.getTokenTypeFromFragment("attributeName", "openParenthesis", "closeParenthesis");
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.addAttributesToTable(attributes);
        server.databases.get(databaseName).tables.put(tableName, table);
    }
}
