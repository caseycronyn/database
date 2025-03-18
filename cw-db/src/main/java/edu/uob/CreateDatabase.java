package edu.uob;

public class CreateDatabase implements DBCommand {
    TokenBank localtokenBank;
    String databaseName;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank){
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        Database database = new Database(databaseName, server.getStorageFolderPath());
        server.databases.put(databaseName, database);
        return "[OK]";
    }
}
