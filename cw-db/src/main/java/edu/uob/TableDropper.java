package edu.uob;

import java.io.File;

public class TableDropper implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        Database currentDatabase = dbManager.getCurrentDatabase();
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        currentDatabase.removeTable(tableName);

        File file = new File(dbManager.getStorageFolderPath() + File.separator + dbManager.getCurrentDatabase() + File.separator + tableName + ".tab");
        file.delete();
        return "[OK]";
    }
}
