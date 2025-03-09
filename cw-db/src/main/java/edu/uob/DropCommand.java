package edu.uob;

public class DropCommand extends Parser {
    String currentToken;
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        currentToken = tokenBank.getCurrentToken();
        if (currentToken.equals("DATABASE")) {
            DBCommand dropDatabase = new DropDatabase();
            return dropDatabase.parse(tokenBank);
        }
        if (currentToken.equals("TABLE")) {
            DBCommand dropTable = new DropTable();
            return dropTable.parse(tokenBank);
        }
//        tokenBank.nextToken();
//        System.out.println(tokenBank.getCurrentToken());
        return this;
    }
}
