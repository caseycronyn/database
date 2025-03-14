package edu.uob;

public class AlterTable extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String alterationType = tokenBank.getTokenFromType("alterationType").getName();
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        String attributeName = tokenBank.getTokenFromType("attributeName").getName();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);

        if (alterationType.equals("ADD")) {
            table.addNewAttribute(attributeName);
        }
        else if (alterationType.equals("DROP")) {
            table.removeAttribute(attributeName);;
        }
    }
}
