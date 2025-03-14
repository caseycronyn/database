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
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        String query = tokenBank.getQueryAsTokenTypes();
        if (tokenBank.getQueryAsTokenType("use").equals(query)) {
            DBCommand useCommand = new UseCommand();
            return useCommand.parse(tokenBank);
        }
        else if (tokenBank.getQueryAsTokenType("create database").equals(query)) {
            DBCommand createDatabase = new CreateDatabase();
            return createDatabase.parse(tokenBank);
        }
        else if (tokenBank.getQueryAsTokenType("create table").equals(query)) {
            DBCommand createTable = new CreateTable();
            return createTable.parse(tokenBank);
        }
        else if (tokenBank.getQueryAsTokenType("create table with attributes").equals(query)) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        //     DBCommand createCommand = new CreateCommand();
        //     return createCommand.parse(tokenBank);
            // case "DROP":
            //     DBCommand dropCommand = new DropCommand();
            //     return dropCommand.parse(tokenBank);
            // case "ALTER":
            //     DBCommand alterCommand = new AlterCommand();
            //     return alterCommand.parse(tokenBank);
            // case "INSERT":
            //     DBCommand insertCommand = new InsertCommand();
            //     return insertCommand.parse(tokenBank);
            // case "SELECT":
            //     DBCommand selectCommand = new SelectCommand();
            //     return selectCommand.parse(tokenBank);
            // default:
            //     return this;
        return this;
    }


    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) {};

    public List<Token> getAttributesFromParenthesis(TokenBank tokenBank) {
        int start = tokenBank.getPositionFromType("openParenthesis");
        Token token = tokenBank.getTokenAtPosition(start + 1);
        List<Token> attributes = new ArrayList<>();
        while (!token.getTokenType().equals("closeParenthesis")) {
            if (token.getTokenType().equals("attributeName")) {
                attributes.add(token);
            }
        }
        return attributes;
    }

}
