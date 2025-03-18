package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class CreateTableWithAttributes implements DBCommand{

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String databaseName = server.getCurrentDatabase();
        List<Token> attributes = tokenBank.getTokenTypeFromFragment("attributeName", "openParenthesis", "closeParenthesis");
        Table table = new Table(tableName, databaseName, server.getStorageFolderPath());
        table.addAttributesToTable(attributes);
        server.databases.get(databaseName).tables.put(tableName, table);
        return "[OK]";
    }
}
