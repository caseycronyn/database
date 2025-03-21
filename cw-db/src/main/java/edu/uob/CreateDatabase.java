package edu.uob;

public class CreateDatabase implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank){
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        if (!databaseManager.databaseExists(databaseName)) {
            Database database = new Database(databaseName, databaseManager.getStorageFolderPath());
            databaseManager.databases.put(databaseName, database);
        }
        return "[OK]";
    }
}
