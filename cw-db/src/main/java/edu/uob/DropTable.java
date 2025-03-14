package edu.uob;

import java.io.File;

public class DropTable extends Parser {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        server.databases.get(server.getCurrentDatabase()).tables.remove(tableName);
        File file = new File(server.getStorageFolderPath() + File.separator + server.getCurrentDatabase() + File.separator + tableName + ".tab");
        file.delete();
    }
}
