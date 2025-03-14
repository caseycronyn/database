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
        tokenBank.initialise();
        String query = tokenBank.getQueryAsTokenTypes();
        if (query.equals(tokenBank.getQueryAsTokenType("use"))) {
            DBCommand useCommand = new UseCommand();
            return useCommand.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenType("create database"))) {
            DBCommand createDatabase = new CreateDatabase();
            return createDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenType("create table"))) {
            DBCommand createTable = new CreateTable();
            return createTable.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenType("create table with attributes")) && tokenBank.getTokens().size() > 4) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenType("drop database"))) {
            DBCommand dropDatabase = new DropDatabase();
            return dropDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenType("drop table"))) {
            DBCommand dropTable = new DropTable();
            return dropTable.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenType("alter"))) {
            DBCommand alterTable = new AlterTable();
            return alterTable.parse(tokenBank);
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
        int start = tokenBank.getTokenFromType("openParenthesis").getPosition();
        tokenBank.setCurrentTokenToPosition(start + 1);
        Token token = tokenBank.getCurrentToken();
        List<Token> attributes = new ArrayList<>();
        while (!token.getTokenType().equals("closeParenthesis")) {
            if (token.getTokenType().equals("attributeName")) {
                attributes.add(token);
            }
            token = tokenBank.nextToken();

        }
        return attributes;
    }

}
