package edu.uob;

import java.util.ArrayList;

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
public class Parser {
    int currentToken = 0;
    ArrayList<String> tokens = new ArrayList<>();

    public String parse(ArrayList<String> tokenArray) {
        tokens = tokenArray;
        for (String token : tokens) {
            if (token.equals(";")) {
//                will replace
                break;
            }

            if (token.equals("CREATE")) {
                CreateCommand createCommand = new CreateCommand(nextToken());
                return createCommand.test();
            }
        }
        return null;
    }

    String nextToken() {
        return tokens.get(currentToken + 1);
    }

//    public DBCmd parse(ArrayList<String> tokens) {
//    }
}
