package edu.uob;

public class CreateCommand extends DBCommand {
    String nextToken;

    CreateCommand(String token) {
        this.nextToken = token;
        test();
    }

    String test() {
        if (nextToken.equals("DATABASE")) {
            CreateDatabase createDatabase = new CreateDatabase();
            return createDatabase.test();
        }
        return nextToken;
    }
}
