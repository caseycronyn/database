package edu.uob;

abstract public class DBCmd {
    List<Condition> conditions;
    List<String> colNames;
    List<String> tableNames;
    String DBname;
    String commandType;

    public void parse() {
    }

}
