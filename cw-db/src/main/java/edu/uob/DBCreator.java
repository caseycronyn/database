package edu.uob;

public class DBCreator implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank){
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        if (!DBManager.databaseExists(databaseName)) {
            Database newDatabase = new Database(databaseName, DBManager.getStorageFolderPath());
            DBManager.addDatabase(newDatabase);
        }
        return "[OK]";
    }
}
