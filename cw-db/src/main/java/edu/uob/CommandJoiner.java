package edu.uob;

public class CommandJoiner implements DBCommand{
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        String tableOneName = tokenBank.getTokenFromType("tableOneName").getValue();
        String tableTwoName = tokenBank.getTokenFromType("tableTwoName").getValue();
        String attributeNameOne = tokenBank.getTokenFromType("attributeOneName").getValue();
        String attributeNameTwo = tokenBank.getTokenFromType("attributeTwoName").getValue();
        Database currentDatabase = dbManager.getCurrentDatabase();
        Table joinedTable = currentDatabase.joinTables(tableOneName, tableTwoName, attributeNameOne, attributeNameTwo);
        currentDatabase.addTable(joinedTable);
        return "[OK]\n" + joinedTable.filterTableWithAttributesAndCondition(null, null);
    }
}
