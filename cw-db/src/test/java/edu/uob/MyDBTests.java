package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;


public class MyDBTests {
    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName() {
        String randomName = "";
        for(int i=0; i<10 ;i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will time out if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(10000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    public void testDatabaseCreationWithEmptyTable() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks;");
    }

    @Test
    public void makeDatabaseAndTable() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE test;");
        sendCommandToServer("CREATE TABLE marks;");
    }

    @Test
    public void testDatabaseCreationWithTableAndAttributes() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
    }

    @Test
    public void testDatabaseDrop() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE test;");
        sendCommandToServer("DROP DATABASE test;");
    }

    @Test
    public void testTableDrop() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE test;");
        sendCommandToServer("CREATE TABLE marks;");
        sendCommandToServer("DROP TABLE marks;");
    }

    @Test
    public void testAlterTable() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("ALTER TABLE marks ADD age;");
        sendCommandToServer("ALTER TABLE marks DROP name;");
    }

    @Test
    public void testInsertCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Hello', 22, FALSE);");
    }

    @Test
    public void selectCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("SELECT mark, name FROM marks;");
    }

    @Test
    public void selectCommandWithConditions() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("SELECT mark, name FROM marks WHERE (pass == FALSE);");
    }

    @Test
    public void selectAllCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("SELECT * FROM marks;");
    }


    @Test
    public void selectAllCommandWithCondition() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("SELECT * FROM marks WHERE (pass == FALSE) OR (name == 'Simon');");
    }

    @Test
    public void selectCommandWithMultipleConditions() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 33, FALSE);");
        sendCommandToServer("SELECT mark, name FROM marks WHERE (pass == FALSE) OR pass == TRUE AND mark > 80;");
    }

    @Test
    public void updateCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("UPDATE marks SET mark = 98 WHERE name == 'Simon';");
    }

    @Test
    public void updateCommandMultiplePairs() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("UPDATE marks SET mark = 98, pass = TRUE WHERE name == 'Jeremy';");
    }

    @Test
    public void deleteCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, FALSE);");
        sendCommandToServer("UPDATE marks SET age = 35 WHERE name == 'Simon';");
        sendCommandToServer("DELETE FROM marks WHERE name == 'Simon' OR mark == 38;");
    }

    @Test
    public void joinCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Sion', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Rob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Chris', 20, FALSE);");

        sendCommandToServer("CREATE TABLE coursework (task, submission);");
        sendCommandToServer("INSERT INTO coursework VALUES ('OXO', 3);");
        sendCommandToServer("INSERT INTO coursework VALUES ('DB', 1);");
        sendCommandToServer("INSERT INTO coursework VALUES ('OXO', 4);");
        sendCommandToServer("INSERT INTO coursework VALUES ('STAG', 2);");

        sendCommandToServer("JOIN coursework AND marks ON submission AND id;");
    }

}

