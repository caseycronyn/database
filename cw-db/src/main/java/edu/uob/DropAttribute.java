package edu.uob;

public class DropAttribute extends Parser {
    String tableName;
    String attributeName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tableName = tokenBank.getCurrentToken().getName();
        tokenBank.nextToken();
        attributeName = tokenBank.nextToken().getName();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
        server.databases.get(server.getCurrentDatabase()).tables.get(tableName).removeAttribute(attributeName);;
    };
}
