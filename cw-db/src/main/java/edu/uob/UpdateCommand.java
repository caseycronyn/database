package edu.uob;

import java.io.FileNotFoundException;
import java.util.List;

public class UpdateCommand implements DBCommand {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        return this;
    }

    @Override
    public void executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException {
        List<Token> nameVauePairs = tokenBank.getTokenTypeFromFragment("nameValuePairs", "set", "where");
        List<Token> condition = tokenBank.getTokenTypeFromFragment("condition", "where", "terminator");
        String tableName = tokenBank.getTokenFromType("tableName").getValue();
        Table table = server.databases.get(server.getCurrentDatabase()).tables.get(tableName);
        table.changeValuesInTableWhereCondition(nameVauePairs, condition);
        table.filterTableWithAttributesAndCondition(null, null);
    }
}
