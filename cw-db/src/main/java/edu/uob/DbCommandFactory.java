package edu.uob;

public class DbCommandFactory {

    public DBCommand createCommand(TokenBank tokenBank) throws Exception {
        tokenBank.initialise();
        String query = tokenBank.getCurrentQueryAsTokenTypes();
        if (query.equals(tokenBank.getPatternFromTokenQueriesMap("use"))) {
            return new UseCommand();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("create database"))) {
            return new CreateDatabase();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("create table"))) {
            return new CreateTable();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("create table with attributes")) && tokenBank.getTokens().size() > 4) {
            return new CreateTableWithAttributes();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop database"))) {
            return new DropDatabase();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop table"))) {
            return new DropTable();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("alter"))) {
            return new AlterTable();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("insert"))) {
            return new InsertCommand();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("select"))) {
            return new SelectCommand();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("select all"))) {
            return new SelectAllCommand();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("update"))) {
            return new UpdateCommand();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("delete"))) {
            return new DeleteCommand();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("join"))) {
            return new JoinCommand();
        }
        else {
            throw new Exception("Something has gone wrong in the parsing stage");
        }
    }
}
