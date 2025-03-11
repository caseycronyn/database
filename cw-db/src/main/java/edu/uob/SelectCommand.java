package edu.uob;

public class SelectCommand extends Conditionals implements DBCommand {
    TokenBank localTokenBank;
    String tableName;
//    String

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        if (tokenBank.getTokenAtPosition(4).equals(";")) {
            return this;
        }
        if (tokenBank.getTokenAtPosition(4).equals("WHERE")) {
            DBCommand selectCommandWithCondition = new SelectCommandWithConditional();
            return selectCommandWithCondition.parse(tokenBank);
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){
        tableName = localTokenBank.getTokenAtPosition(3);
        if (localTokenBank.getCurrentToken().equals("*")) {
            server.databases.get(server.getCurrentDatabase()).tables.get(tableName).printTableToStdout();
        }
    };
}
