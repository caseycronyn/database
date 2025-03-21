package edu.uob;

public class CommandUser implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        dbManager.setCurrentDatabase(databaseName);
        return "[OK]";
    }
}
