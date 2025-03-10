package edu.uob;

import java.util.ArrayList;

public class BooleanConditional extends Parser {
    String operator;
    ArrayList<String> arguments;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        Arguments conditions = new Arguments();
        arguments = conditions.buildArguments(tokenBank);
        operator = arguments.get(1);
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
        ;
    }

}

