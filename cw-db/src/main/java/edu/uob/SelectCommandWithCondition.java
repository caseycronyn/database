package edu.uob;

public class SelectCommandWithCondition extends Parser {

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        return null;
    }

    @Override
    public void executeCommand(DBServer server){
        ;
    };
}
