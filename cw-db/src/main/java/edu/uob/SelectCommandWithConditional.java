package edu.uob;

import java.util.ArrayList;

public class SelectCommandWithConditional extends Parser {
    String operator;
    ArrayList<String> arguments;

//    this part should work out what the check will be. parse the conditional and record what kind it is and what it checks
//    should make a conditional checker similar to what dbcommand/ parser is doing that is then used in execute
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
        tokenBank.setTokenAtPosition(5);
        Arguments conditions = new Arguments();
        arguments = conditions.buildArguments(tokenBank);
        FindConditional findConditional = new FindConditional();
        findConditional.parse(arguments);
//        build conditional class
        return this;
    }

//    this part will carry out the check. perhaps another class will need to be called but who knows
    @Override
    public void executeCommand(DBServer server){
        ;
//        loop through rows printing the selected attributes in order when the boolean is true
    }
}
