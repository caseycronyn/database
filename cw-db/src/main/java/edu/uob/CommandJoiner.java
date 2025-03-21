package edu.uob;

public class CommandJoiner implements DBCommand{
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        String tableOneName = tokenBank.getTokenFromType("tableOneName").getValue();
        String tableTwoName = tokenBank.getTokenFromType("tableTwoName").getValue();
        String joinOne = tokenBank.getTokenFromType("attributeOneName").getValue();
        String joinTwo = tokenBank.getTokenFromType("attributeTwoName").getValue();
        Database database = dbManager.getCurrentDatabase();
        Table table = database.combineTablesIntoNewTable(tableOneName, tableTwoName, joinOne, joinTwo);
        database.addTable(table);
        return "[OK]\n" + table.filterTableWithAttributesAndCondition(null, null);
    }
}
