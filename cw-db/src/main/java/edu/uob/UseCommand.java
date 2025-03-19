package edu.uob;

public class UseCommand extends Parser {
    TokenBank tokenBank;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
        return this;
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) {
        String databaseName = tokenBank.getTokenFromType("databaseName").getValue();
        server.setCurrentDatabase(databaseName);
        return "[OK]";
    }
}
