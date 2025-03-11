package edu.uob;

// lets make this very procedural 😊
// the tokens will go in and back out will be spat the right thing
public class Conditionals {

//    recursive conditional. requires the right token passed in.
    void booleanConditional(TokenBank tokenBank) {

        return;
//        evaluate left expression
//        evaluate right expression
//        return left-condition BOOL right-condition
    }

    void comparatorConditional(DBServer server, TokenBank tokenBank) {
/*        *//*
        evaluate attr + comparator + value.
        may need to call other more specific methods for this
         *//*
        String tableName = tokenBank.getTokenAtPosition(3);
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get();
        Integer current = tokenBank.getCurrentTokenPosition();
        String attribute = tokenBank.getCurrentToken();



        String comparator = tokenBank.getTokenAtPosition(current + 1);
        String value = tokenBank.getTokenAtPosition(current + 2);
        if (comparator.equals("==")) {

        }*/
    }
}
