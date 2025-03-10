package edu.uob;

public class ParserTemplate extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
        ;
    }

}

