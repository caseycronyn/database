package edu.uob;

import java.io.File;

public class DBDeleter implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        DBManager.databases.remove(databaseName);
        File file = new File(DBManager.getStorageFolderPath() + File.separator + databaseName);
        DBManager.deleteDirectory(file);
        if (!file.delete()) {
            return ("[ERROR] deleting " + databaseName);
        }
        return "[OK]";
    }
}
