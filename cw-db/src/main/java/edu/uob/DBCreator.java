package edu.uob;

public class DBCreator implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank){
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        if (!DBManager.databaseExists(databaseName)) {
            Database database = new Database(databaseName, DBManager.getStorageFolderPath());
            DBManager.databases.put(databaseName, database);
        }
        return "[OK]";
    }
}
