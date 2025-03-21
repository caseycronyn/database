package edu.uob;

import java.io.File;

public class TableDropper implements DBCommand {
    @Override
    public String executeCommand(DBManager dbManager, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        dbManager.getCurrentDatabase().tables.remove(tableName);
        File file = new File(dbManager.getStorageFolderPath() + File.separator + dbManager.getCurrentDatabase() + File.separator + tableName + ".tab");
        file.delete();
        return "[OK]";
    }
}
