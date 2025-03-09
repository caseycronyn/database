package edu.uob;

import java.util.ArrayList;
import java.util.List;

/*
Parser builds subclass of
DBcmd as it parses. This is
returned to DBServer which
passes a reference to itself
as an argument to the method
used to execute the command.
The context given by parsing
        the BNF, combined with Token
types facilitates error
handling and command building
 */
public class Parser implements DBCommand {

    // need to change this so I'm, checking for the first token only
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        for (int i = 0; i < tokenBank.tokens.size(); i++) {
            if (tokenBank.tokens.get(i).equals(";")) {
//                will replace
                return this;
            }

            if (tokenBank.tokens.get(i).equals("CREATE")) {
                tokenBank.nextToken();
                DBCommand createCommand = new CreateCommand();
                return createCommand.parse(tokenBank);
            }

            if (tokenBank.tokens.get(i).equals("USE")) {
                tokenBank.nextToken();
                DBCommand useCommand = new UseCommand();
                return useCommand.parse(tokenBank);
            }

            if (tokenBank.tokens.get(i).equals("DROP")) {
                tokenBank.nextToken();
                DBCommand dropCommand = new DropCommand();
                return dropCommand.parse(tokenBank);
            }
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){};

//    public DBCmd parse(ArrayList<String> tokens) {
//    }
}
