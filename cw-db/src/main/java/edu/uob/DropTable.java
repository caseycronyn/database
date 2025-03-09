package edu.uob;

import java.io.File;

public class DropTable extends Parser {
    String tableName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tableName = tokenBank.getNextToken();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        server.databases.get(server.getCurrentDatabase()).tables.remove(tableName);
        System.out.println(server.databases.get(server.getCurrentDatabase()).tables);
//        System.out.println(server.getCurrentDatabase());
//        System.out.println(server.databases.get(server.getCurrentDatabase()));
//        File file = new File(server.getStorageFolderPath() + File.separator + server.getCurrentDatabase() + File.separator + tableName);
//        file.delete();
    }
}
