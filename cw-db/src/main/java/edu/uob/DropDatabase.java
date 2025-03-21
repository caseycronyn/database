package edu.uob;

import java.io.File;

public class DropDatabase implements DBCommand {
    @Override
    public String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        databaseManager.databases.remove(databaseName);
        File file = new File(databaseManager.getStorageFolderPath() + File.separator + databaseName);
        databaseManager.deleteDirectory(file);
        if (!file.delete()) {
            return ("[ERROR] deleting " + databaseName);
        }
        return "[OK]";
    }
}
