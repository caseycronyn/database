package edu.uob;

import java.io.FileNotFoundException;

public class TableAlterer implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException {
        Database currentDatabase = DBManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = currentDatabase.tableMap.get(tableName);

        String alterationType = tokenBank.getTokenFromType("alterationType").getValue();
        String attributeName = tokenBank.getTokenFromType("attributeName").getValue();
        if (alterationType.equals("ADD")) {
            table.addAttribute(attributeName);
        }
        else if (alterationType.equals("DROP")) {
            table.removeAttribute(attributeName);
        }
        return "[OK]";
    }
}
