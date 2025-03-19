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
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.initialise();
        String query = tokenBank.getQueryAsTokenTypes();
        if (query.equals(tokenBank.getQueryAsTokenTypes("use"))) {
            DBCommand useCommand = new UseCommand();
            return useCommand.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("create database"))) {
            DBCommand createDatabase = new CreateDatabase();
            return createDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("create table"))) {
            DBCommand createTable = new CreateTable();
            return createTable.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("create table with attributes")) && tokenBank.getTokens().size() > 4) {
            DBCommand createTableWithAttributes = new CreateTableWithAttributes();
            return createTableWithAttributes.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("drop database"))) {
            DBCommand dropDatabase = new DropDatabase();
            return dropDatabase.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("drop table"))) {
            DBCommand dropTable = new DropTable();
            return dropTable.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("alter"))) {
            DBCommand alterTable = new AlterTable();
            return alterTable.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("insert"))) {
            DBCommand insertCommand = new InsertCommand();
            return insertCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("select"))) {
            DBCommand selectCommand = new SelectCommand();
            return selectCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("select all"))) {
            DBCommand selectAllCommand = new SelectAllCommand();
            return selectAllCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("update"))) {
            DBCommand updateCommand = new UpdateCommand();
            return updateCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenTypes("delete"))) {
            DBCommand deleteCommand = new DeleteCommand();
            return deleteCommand.parse(tokenBank);
        }
        else if (query.equals(tokenBank.getQueryAsTokenTypes("join"))) {
            DBCommand joinCommand = new JoinCommand();
            return joinCommand.parse(tokenBank);
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
    public String executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException {
        return null;
    }
}
