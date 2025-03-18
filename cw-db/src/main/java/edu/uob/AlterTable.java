package edu.uob;

import java.io.FileNotFoundException;

public class AlterTable implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException {
        String alterationType = tokenBank.getTokenFromType("alterationType").getValue();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        String attributeName = tokenBank.getTokenFromType("attributeName").getValue();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);

        if (alterationType.equals("ADD")) {
            table.addNewAttribute(attributeName);
        }
        else if (alterationType.equals("DROP")) {
            table.removeAttribute(attributeName);;
        }
        return "[OK]";
    }
}
