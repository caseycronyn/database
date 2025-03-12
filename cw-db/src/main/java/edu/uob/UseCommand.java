package edu.uob;

public class UseCommand extends Parser {
    String databaseName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
//        databaseName = tokenBank.getCurrentToken();
        databaseName = tokenBank.getNextToken().getName();
//        System.out.println(tokenBank.getCurrentToken());
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        System.out.println(databaseName);
        server.setCurrentDatabase(databaseName);
    };
}
