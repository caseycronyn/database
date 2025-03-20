package edu.uob;

import java.io.FileNotFoundException;

public interface DBCommand {
    abstract DBCommand parse(TokenBank tokenBank) throws Exception;
    abstract String executeCommand(DBServer server, TokenBank tokenBank) throws FileNotFoundException;
}
