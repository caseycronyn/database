package edu.uob;

public class CreateCommand extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
//        System.out.println(DatabaseName);
//        testInteger += 1;
//        System.out.println(testInteger);
        if (tokenBank.getCurrentToken().equals("DATABASE")) {
//            tokenBank.nextToken();
            DBCommand createDatabase = new CreateDatabase();
            return createDatabase.parse(tokenBank);
        } else if (tokenBank.getCurrentToken().equals("TABLE")) {
            DBCommand createTable = new CreateTable();
            return createTable.parse(tokenBank);
        }
        return null;
    }

//    @Override
//    public void executeCommand(DBServer server){};
}