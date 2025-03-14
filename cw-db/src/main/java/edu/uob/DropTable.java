package edu.uob;

import java.io.File;

public class DropTable extends Parser {
    String tableName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tableName = tokenBank.nextToken().getName();
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        server.databases.get(server.getCurrentDatabase()).tables.remove(tableName);
        File file = new File(server.getStorageFolderPath() + File.separator + server.getCurrentDatabase() + File.separator + tableName + ".tab");
        file.delete();
    }
}
