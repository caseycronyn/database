package edu.uob;

public class DbCommandFactory {

    public DBCommand createCommand(TokenBank tokenBank) throws Exception {
        tokenBank.initialise();
        String query = tokenBank.getQueryAsTypes();
        if (query.equals(tokenBank.getTokenTypeQuery("use"))) {
            return new CommandUser();
        }
        else if (query.equals(tokenBank.getTokenTypeQuery("create database"))) {
            return new DBCreator();
        }
        else if (query.contains(tokenBank.getTokenTypeQuery("create table"))) {
            return new TableCreator();
        }
        else if (query.equals(tokenBank.getTokenTypeQuery("drop database"))) {
            return new DBDeleter();
        }
        else if (query.equals(tokenBank.getTokenTypeQuery("drop table"))) {
            return new TableDropper();
        }
        else if (query.equals(tokenBank.getTokenTypeQuery("alter"))) {
            return new TableAlterer();
        }
        else if (query.contains(tokenBank.getTokenTypeQuery("insert"))) {
            return new CommandInserter();
        }
        else if (query.contains(tokenBank.getTokenTypeQuery("select"))) {
            return new CommandSelector();
        }
        else if (query.contains(tokenBank.getTokenTypeQuery("update"))) {
            return new CommandUpdater();
        }
        else if (query.contains(tokenBank.getTokenTypeQuery("delete"))) {
            return new CommandDeleter();
        }
        else if (query.equals(tokenBank.getTokenTypeQuery("join"))) {
            return new CommandJoiner();
        }
        else {
            throw new Exception("Something has gone wrong in the parsing stage");
        }
    }
}
