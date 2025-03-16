package edu.uob;

public class SelectAllCommand implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        String tableName = tokenBank.getTokenFromType("tableName").getName();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        table.printTableToStdout(null, null);
    }
}
