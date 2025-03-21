package edu.uob;

public class CommandJoiner implements DBCommand{
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableOneName = tokenBank.getTokenFromType("tableOneName").getValue();
        String tableTwoName = tokenBank.getTokenFromType("tableTwoName").getValue();
        String attributeNameOne = tokenBank.getTokenFromType("attributeOneName").getValue();
        String attributeNameTwo = tokenBank.getTokenFromType("attributeTwoName").getValue();

        Table joinedTable = currentDatabase.joinTables(tableOneName, tableTwoName, attributeNameOne, attributeNameTwo);
        currentDatabase.addTable(joinedTable);
        return "[OK]\n" + joinedTable.filterTable(null, null);
    }
}
