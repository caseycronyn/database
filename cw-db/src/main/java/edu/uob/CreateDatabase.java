package edu.uob;

public class CreateDatabase extends DBCommand {

    public String test(TokenBank tokenBank) {
        tokenBank.nextToken();
        return "CREATE DATABASE " + tokenBank.getCurrentToken();
    }
}
