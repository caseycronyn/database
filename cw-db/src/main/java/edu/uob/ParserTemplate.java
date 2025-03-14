package edu.uob;

public class ParserTemplate extends Parser {
    TokenBank localTokenBank;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        ;
    }

}

