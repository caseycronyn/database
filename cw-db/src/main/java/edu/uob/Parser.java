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
public class Parser extends Lexer implements DBCommand {

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
        else if (query.contains(tokenBank.getQueryAsTokenType("insert"))) {
            DBCommand insertCommand = new InsertCommand();
            return insertCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenType("select"))) {
            DBCommand selectCommand = new SelectCommand();
            return selectCommand.parse(tokenBank);
        }
        else if (query.contains(tokenBank.getQueryAsTokenType("select all"))) {
            DBCommand selectAllCommand = new SelectAllCommand();
            return selectAllCommand.parse(tokenBank);
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
    public void executeCommand(DBServer server, TokenBank tokenBank) {
    }

    ;


    // public List<Token>



}
