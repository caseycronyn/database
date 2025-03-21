package edu.uob;

import java.io.FileNotFoundException;

public interface DBCommand {
    String executeCommand(DBManager DBManager, TokenBank tokenBank) throws FileNotFoundException;
}
