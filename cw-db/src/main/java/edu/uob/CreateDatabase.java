package edu.uob;

import java.util.ArrayList;

public class CreateDatabase extends Parser {
    TokenBank tokenBankLocal = new TokenBank();

    @Override
    public DBCommand parse(TokenBank tokenBankIn) {
        tokenBankLocal = tokenBankIn;
        tokenBankLocal.nextToken();
//        return "CREATE DATABASE " + tokenBank.getCurrentToken();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(tokenBankLocal.getCurrentToken());
        server.createNewDatabase(tokenBankLocal.getCurrentToken());
    };

//    public void createNewDatabase(String databaseName) {
//        Database db = new Database(databaseName, storageFolderPath);
//        databases.add(db);
//    }
}
