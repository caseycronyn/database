package edu.uob;

public class JoinCommand implements DBCommand{
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        String tableOneName = tokenBank.getTokenFromType("tableOneName").getValue();
        String tableTwoName = tokenBank.getTokenFromType("tableTwoName").getValue();
        String joinOne = tokenBank.getTokenFromType("attributeOneName").getValue();
        String joinTwo = tokenBank.getTokenFromType("attributeTwoName").getValue();
        Database database = server.databases.get(server.getCurrentDatabase());
        Table table = database.combineTablesIntoNewTable(tableOneName, tableTwoName, joinOne, joinTwo);
        database.addTable(table);
        return "[OK]\n" + table.filterTableWithAttributesAndCondition(null, null);
    }
}
