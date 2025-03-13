package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
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
    TokenBank localTokenBank;
    String[] specialCharacters = {"(", ")", ",", ";"};
    // need to change this so I'm, checking for the first token only
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        Token token = tokenBank.getFirstToken();
        switch (token.getName()) {
            case ";":
                return this;
            case "CREATE":
                DBCommand createCommand = new CreateCommand();
                return createCommand.parse(tokenBank);
            case "USE":
                DBCommand useCommand = new UseCommand();
                return useCommand.parse(tokenBank);
            case "DROP":
                DBCommand dropCommand = new DropCommand();
                return dropCommand.parse(tokenBank);
            case "ALTER":
                DBCommand alterCommand = new AlterCommand();
                return alterCommand.parse(tokenBank);
            case "INSERT":
                DBCommand insertCommand = new InsertCommand();
                return insertCommand.parse(tokenBank);
            case "SELECT":
                DBCommand selectCommand = new SelectCommand();
                return selectCommand.parse(tokenBank);
        }
        return null;
    }

    @Override
    public void executeCommand(DBServer server){};

   // tries to get rid of the parenthesis and get the arguments inside as a list
    public ArrayList<String> convertListInParenthesisToArray(TokenBank tokenBank) {
        int position = tokenBank.getCurrentTokenPosition();
        String currentToken;
        ArrayList<String> fragments = new ArrayList<>();
        for (; position < tokenBank.tokens.size(); position++) {
            currentToken = tokenBank.tokens.get(position).getName();
            if (!Arrays.asList(specialCharacters).contains(currentToken)) {
            // System.out.println(tokenBank.tokens.get(position));
                fragments.add(tokenBank.tokens.get(position).getName());
            }
        }
       // String str = String.join("", fragments);
       // System.out.println(fragments);
        return fragments;
    }

   // public DBCmd parse(ArrayList<String> tokens) {
   // }
}
