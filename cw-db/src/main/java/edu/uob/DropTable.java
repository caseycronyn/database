package edu.uob;

public class DropTable extends Parser {
    String tableName;

    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tableName = tokenBank.getNextToken();
        return this;
    }

    @Override
    public void executeCommand(DBServer server){
//        server.removeTable(tableName);
    };
}
