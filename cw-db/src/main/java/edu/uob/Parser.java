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
        for (int i = 0; i < tokenBank.tokens.size(); i++) {
            if (tokenBank.tokens.get(i).getName().equals(";")) {
               // will replace
                return this;
            }

            if (tokenBank.tokens.get(i).getName().equals("CREATE")) {
                tokenBank.nextToken();
                DBCommand createCommand = new CreateCommand();
                return createCommand.parse(tokenBank);
            }

            if (tokenBank.tokens.get(i).getName().equals("USE")) {
                tokenBank.nextToken();
                DBCommand useCommand = new UseCommand();
                return useCommand.parse(tokenBank);
            }

            if (tokenBank.tokens.get(i).getName().equals("DROP")) {
                tokenBank.nextToken();
                DBCommand dropCommand = new DropCommand();
                return dropCommand.parse(tokenBank);
            }
            if (tokenBank.tokens.get(i).getName().equals("ALTER")) {
                tokenBank.nextToken();
                DBCommand alterCommand = new AlterCommand();
                return alterCommand.parse(tokenBank);
            }
            if (tokenBank.tokens.get(i).getName().equals("INSERT")) {
                tokenBank.nextToken();
                DBCommand insertCommand = new InsertCommand();
                return insertCommand.parse(tokenBank);
            }
            if (tokenBank.tokens.get(i).getName().equals("SELECT")) {
                tokenBank.nextToken();
                DBCommand selectCommand = new SelectCommand();
                return selectCommand.parse(tokenBank);
            }
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
