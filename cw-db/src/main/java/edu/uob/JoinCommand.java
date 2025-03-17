package edu.uob;

public class JoinCommand implements DBCommand{
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableOneName = tokenBank.getTokenFromType("tableOneName").getName();
        String tableTwoName = tokenBank.getTokenFromType("tableTwoName").getName();
        String joinOne = tokenBank.getTokenFromType("attributeOneName").getName();
        String joinTwo = tokenBank.getTokenFromType("attributeTwoName").getName();
        Database database = server.databases.get(server.getCurrentDatabase());
        Table table = database.combineTablesIntoNewTable(tableOneName, tableTwoName, joinOne, joinTwo);
        database.addTable(table);
    }
}
