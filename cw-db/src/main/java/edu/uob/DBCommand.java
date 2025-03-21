package edu.uob;

import java.io.FileNotFoundException;

public interface DBCommand {
    String executeCommand(DatabaseManager databaseManager, TokenBank tokenBank) throws FileNotFoundException;
}
