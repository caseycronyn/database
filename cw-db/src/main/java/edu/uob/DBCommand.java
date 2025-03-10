package edu.uob;

import java.util.ArrayList;
import java.util.List;

public interface DBCommand {
//    List<Condition> conditions;
//    List<String> colNames = null;
//    List<String> tableNames = null;
    String[] specialCharacters = {"(", ")", ",", ";"};
    TokenBank tokenBankLocal = null;

    abstract DBCommand parse(TokenBank tokenBank);
    abstract void executeCommand(DBServer server);
//    String commandType;
//

}
