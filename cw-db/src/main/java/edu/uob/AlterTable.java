package edu.uob;

import java.io.FileNotFoundException;

public class AlterTable implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) throws FileNotFoundException {
        String alterationType = tokenBank.getTokenFromType("alterationType").getValue();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String attributeName = tokenBank.getTokenFromType("attributeName").getValue();
        Table table = databaseManager.getCurrentDatabase().tables.get(tableName);

        if (alterationType.equals("ADD")) {
            table.addNewAttribute(attributeName);
        }
        else if (alterationType.equals("DROP")) {
            table.removeAttribute(attributeName);
        }
        return "[OK]";
    }
}
