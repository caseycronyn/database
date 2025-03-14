package edu.uob;

import java.util.ArrayList;

public class BooleanConditional extends Parser {
    String operator;
    ArrayList<String> arguments;
    TokenBank localTokenBank;

    // @Override
    // public DBCommand parse(TokenBank tokenBank) {
    //     localTokenBank = tokenBank;
    //     arguments = convertListInParenthesisToArray(tokenBank);
    //     operator = arguments.get(1);
    //     return this;
    // }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {
        ;
    }

}

