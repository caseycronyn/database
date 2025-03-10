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
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    public void testDatabaseCreationWithTableAndAttributes() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE DATABASE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
    }

    @org.junit.jupiter.api.Test
    public void testDatabaseCreationWithEmptyTable() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE DATABASE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks;");
    }

    @org.junit.jupiter.api.Test
    public void makeDatabaseAndTable() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE DATABASE test;");
        sendCommandToServer("CREATE TABLE marks;");
    }

    @org.junit.jupiter.api.Test
    public void testDatabaseDrop() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE DATABASE test;");
        sendCommandToServer("DROP DATABASE test;");
    }

    @org.junit.jupiter.api.Test
    public void testTableDrop() {
        sendCommandToServer("CREATE DATABASE test;");
        sendCommandToServer("USE DATABASE test;");
        sendCommandToServer("CREATE TABLE marks;");
        sendCommandToServer("DROP TABLE marks;");
    }

    @org.junit.jupiter.api.Test
    public void testAlterTable() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE DATABASE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("ALTER TABLE marks ADD age;");
        sendCommandToServer("ALTER TABLE marks DROP name;");
    }

    @Test
    public void testInsertCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE DATABASE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
    }

    @Test
    public void selectCommand() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE DATABASE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('David', 82, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Jeremy', 38, False);");
        sendCommandToServer("SELECT * FROM marks;");
    }

//    @Test
//    public void selectCommandWithCondition() {
//        String randomName = generateRandomName();
//        sendCommandToServer("CREATE DATABASE " + randomName + ";");
//        sendCommandToServer("USE DATABASE " + randomName + ";");
//        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
//        sendCommandToServer("SELECT * FROM marks;");
//    }

}

