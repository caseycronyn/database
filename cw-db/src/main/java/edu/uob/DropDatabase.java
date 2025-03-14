package edu.uob;

import java.io.File;

public class DropDatabase extends Parser {
    String databaseName;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        databaseName = tokenBank.nextToken().getName();
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
//        server.deleteDatabase(databaseName);
        server.databases.remove(databaseName);
        File file = new File(server.getStorageFolderPath() + File.separator + databaseName);
        server.deleteDirectory(file);
        file.delete();
    };
}
