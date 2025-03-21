package edu.uob;

public class UseCommand implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager dbManager, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        dbManager.setCurrentDatabase(databaseName);
        return "[OK]";
    }
}
