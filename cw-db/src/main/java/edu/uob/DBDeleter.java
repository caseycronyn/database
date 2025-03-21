package edu.uob;

import java.io.File;

public class DBDeleter implements DBCommand {
    @Override
    public String executeCommand(DBManager DBManager, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        DBManager.databases.remove(databaseName);

        File newFile = new File(DBManager.getStorageFolderPath() + File.separator + databaseName);
        DBManager.deleteDirectory(newFile);
        if (!newFile.delete()) {
            return ("[ERROR] deleting " + databaseName);
        }
        return "[OK]";
    }
}
