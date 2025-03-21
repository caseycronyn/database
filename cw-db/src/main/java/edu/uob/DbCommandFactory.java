package edu.uob;

public class DbCommandFactory {

    public DBCommand createCommand(TokenBank tokenBank) throws Exception {
        tokenBank.initialise();
        String query = tokenBank.getCurrentQueryAsTokenTypes();
        if (query.equals(tokenBank.getPatternFromTokenQueriesMap("use"))) {
            return new CommandUser();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("create database"))) {
            return new DBCreator();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("create table"))) {
            return new TableCreator();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop database"))) {
            return new DBDeleter();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("drop table"))) {
            return new TableDropper();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("alter"))) {
            return new TableAlterer();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("insert"))) {
            return new CommandInserter();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("select"))) {
            return new CommandSelector();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("update"))) {
            return new CommandUpdater();
        }
        else if (query.contains(tokenBank.getPatternFromTokenQueriesMap("delete"))) {
            return new CommandDeleter();
        }
        else if (query.equals(tokenBank.getPatternFromTokenQueriesMap("join"))) {
            return new CommandJoiner();
        }
        else {
            throw new Exception("Something has gone wrong in the parsing stage");
        }
    }
}
