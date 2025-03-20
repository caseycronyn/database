package edu.uob;

import java.io.FileNotFoundException;
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
    public DBCommand parse(TokenBank tokenBank) throws Exception {
        tokenBank.initialise();
        String query = tokenBank.getCurrentQueryAsTokenTypes();
        if (query.equals(tokenBank.getPatternFromTokenQueriesMap("use"))) {
            DBCommand useCommand = new UseCommand();
            return useCommand.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("create database"))) {
            DBCommand createDatabase = new CreateDatabase();
            return createDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("create table"))) {
            DBCommand createTable = new CreateTable();
            return createTable.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("create table with attributes")) && tokenBank.getTokens().size() > 4) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop database"))) {
            DBCommand dropDatabase = new DropDatabase();
            return dropDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop table"))) {
            DBCommand dropTable = new DropTable();
            return dropTable.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("alter"))) {
            DBCommand alterTable = new AlterTable();
            return alterTable.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("insert"))) {
            DBCommand insertCommand = new InsertCommand();
            return insertCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("select"))) {
            DBCommand selectCommand = new SelectCommand();
            return selectCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("select all"))) {
            DBCommand selectAllCommand = new SelectAllCommand();
            return selectAllCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("update"))) {
            DBCommand updateCommand = new UpdateCommand();
            return updateCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("delete"))) {
            DBCommand deleteCommand = new DeleteCommand();
            return deleteCommand.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("join"))) {
            DBCommand joinCommand = new JoinCommand();
            return joinCommand.parse(tokenBank);
        }
        else {
            throw new Exception("Something has gone wrong in the parsing stage");
        }
    }

    @Override
    public String executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException {
        return null;
    }
}
