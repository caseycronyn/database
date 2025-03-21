package edu.uob;

import java.io.FileNotFoundException;

public class TableAlterer implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException {
        String alterationType = tokenBank.getTokenFromType("alterationType").getValue();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String attributeName = tokenBank.getTokenFromType("attributeName").getValue();
        Table table = DBManager.getCurrentDatabase().tableMap.get(tableName);

        if (alterationType.equals("ADD")) {
            table.addNewAttribute(attributeName);
        }
        else if (alterationType.equals("DROP")) {
            table.removeAttribute(attributeName);
        }
        return "[OK]";
    }
}
