package edu.uob;

import java.util.ArrayList;

public class CreateDatabase extends Parser {
//    TokenBank tokenBankLocal = new TokenBank();
    String databaseName = null;

    @Override
    public DBCommand parse(TokenBank tokenBankIn) {
//        tokenBankLocal = tokenBankIn;
//        tokenBankIn.nextToken();
        databaseName = tokenBankIn.nextToken().getName();
//        return "CREATE DATABASE " + tokenBank.getCurrentToken();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(tokenBankLocal.getCurrentToken());
        Database database = new Database(databaseName, server.getStorageFolderPath());
        server.databases.put(databaseName, database);
    };

//    public void createNewDatabase(String databaseName) {
//        Database db = new Database(databaseName, storageFolderPath);
//        databases.add(db);
//    }
}
