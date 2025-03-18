package edu.uob;

import java.io.File;

public class DropTable implements DBCommand {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        server.databases.get(server.getCurrentDatabase()).tables.remove(tableName);
        File file = new File(server.getStorageFolderPath() + File.separator + server.getCurrentDatabase() + File.separator + tableName + ".tab");
        if (!file.delete()) {
            System.err.println("Could not delete file: " + file.getAbsolutePath());
        }
        return "[OK]";
    }
}
