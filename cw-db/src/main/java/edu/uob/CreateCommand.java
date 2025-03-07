package edu.uob;

public class CreateCommand extends DBCommand {
    String test(TokenBank tokenBank) {
        if (tokenBank.getCurrentToken().equals("DATABASE")) {
//            tokenBank.nextToken();
            CreateDatabase createDatabase = new CreateDatabase();
            return createDatabase.test(tokenBank);
        }
        return null;
    }
}
