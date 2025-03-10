package edu.uob;

public class AddAttribute extends Parser {
    String attributeName;
    String tableName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
//        tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
        tableName = tokenBank.getCurrentToken();
        tokenBank.nextToken();
        attributeName = tokenBank.getNextToken();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(server.databases.get(server.getCurrentDatabase()).tables.get(tableName));
        server.databases.get(server.getCurrentDatabase()).tables.get(tableName).addNewAttribute(attributeName);
    };
}
