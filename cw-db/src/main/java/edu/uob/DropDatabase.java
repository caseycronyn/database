package edu.uob;

import java.io.File;

public class DropDatabase extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        server.databases.remove(databaseName);
        File file = new File(server.getStorageFolderPath() + File.separator + databaseName);
        server.deleteDirectory(file);
        if (!file.delete()) {
            System.out.println("Error deleting " + databaseName);
        }
    };
}
