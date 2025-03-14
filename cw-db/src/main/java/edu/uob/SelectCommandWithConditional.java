package edu.uob;

import java.util.ArrayList;

public class SelectCommandWithConditional extends Conditionals implements DBCommand {
    String operator;
    ArrayList<String> arguments;
    TokenBank localTokenBank;

//    this part should work out what the check will be. parse the conditional and record what kind it is and what it checks
//    should make a conditional checker similar to what dbcommand/ parser is doing that is then used in execute
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        localTokenBank = tokenBank;
//        set token to start of conditional fragment
        tokenBank.setCurrentTokenToPosition(5);
        // booleanConditional(tokenBank);
//        build conditional class
        return this;
    }

//    this part will carry out the check. perhaps another class will need to be called but who knows
    @Override
    public void executeCommand(DBServer server, TokenBank tokenbank) {
        comparatorConditional(server, localTokenBank);
//        conditional checks with tokens passed in will return a conditional check that we can pass into the method ...
//        table.loopThroughTableAndApplySelection(boolean check);
//        loop through rows printing the selected attributes in order when the boolean is true
    }
}
