package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


public class MyDBTests {
    HashMap<String, String> queriesMap = new HashMap<>();
    private DBServer server;
    String[] longResponse;
    String good = "[OK]";

    void setupArrays() {
          longResponse = new String[] {     "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n2\tSion\t55\tTRUE\n3\tRob\t35\tFALSE\n4\tChris\t20\tFALSE\n",
          "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n3\tRob\t35\tFALSE\n4\tChris\t20\tFALSE\n",
          "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n2\tSion\t55\tTRUE\n",
          "[OK]\nid\ttask\tsubmission\n5\tOXO\t3\n6\tDB\t1\n7\tOXO\t4\n8\tSTAG\t2\n",
          "[OK]\nid\tcoursework.task\tmarks.name\tmarks.mark\tmarks.pass\n9\tOXO\tRob\t35\tFALSE\n10\tDB\tSimon\t65\tTRUE\n11\tOXO\tChris\t20\tFALSE\n12\tSTAG\tSion\t55\tTRUE\n",
          "[OK]\nid\tname\tmark\tpass\n4\tChris\t38\tFALSE\n",
          "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n3\tRob\t35\tFALSE\n4\tChris\t38\tFALSE\n",
          "[OK]\nid\tname\tmark\tpass\n4\tChris\t38\tFALSE\n",
          "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n4\tChris\t38\tFALSE\n",
          "[OK]\nid\n3\n4\n",
          "[OK]\nname\nSimon\n",
          "[OK]\nid\tname\tmark\tpass\n1\tSimon\t65\tTRUE\n",
          "[OK]\nid\tname\tmark\tpass\tage\n1\tSimon\t65\tTRUE\n",
          "[OK]\nid\tname\tmark\tpass\tage\n1\tSimon\t65\tTRUE\t35\n",
          "[OK]\nid\tname\tmark\tage\n1\tSimon\t65\t35\n",
          "[ERROR]: Semi colon missing at end of line",
          "[ERROR]: Table does not exist",
          "[ERROR]: Attribute does not exist"
       };

        queriesMap.put("CREATE DATABASE markbook;", good);
        queriesMap.put("USE markbook;", good);
        queriesMap.put("CREATE TABLE marks (name, mark, pass);", good);
        queriesMap.put("INSERT INTO marks VALUES ('Simon', 65, TRUE);", good);
        queriesMap.put("INSERT INTO marks VALUES ('Sion', 55, TRUE);", good);
        queriesMap.put("INSERT INTO marks VALUES ('Rob', 35, FALSE);", good);
        queriesMap.put("INSERT INTO marks VALUES ('Chris', 20, FALSE);", good);
        queriesMap.put("SELECT * FROM marks;", longResponse[0]);
        queriesMap.put("SELECT * FROM marks WHERE name != 'Sion';", longResponse[1]);
        queriesMap.put("SELECT * FROM marks WHERE pass == TRUE;", longResponse[2]);
        // Assuming there is a table called “coursework” in the database (and that table has been filled with data)
        queriesMap.put("SELECT * FROM coursework;", longResponse[3]);
        // For JOINs: discard the ids from the original tables discard the columns that the tables were matched on create a new unique id for each of row of the table produced attribute names are prepended with name of table from which they originated
        queriesMap.put("JOIN coursework AND marks ON submission AND id;", longResponse[4]);
        queriesMap.put("UPDATE marks SET mark = 38 WHERE name == 'Chris';", good);
        queriesMap.put("SELECT * FROM marks WHERE name == 'Chris';", longResponse[5]);
        // queriesMap.put("SELECT * FROM marks;", longResponse[6]);
        queriesMap.put("DELETE FROM marks WHERE name == 'Sion';", good);
        queriesMap.put("SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);", longResponse[7]);
        queriesMap.put("SELECT * FROM marks WHERE name LIKE 'i';", longResponse[8]);
        queriesMap.put("SELECT id FROM marks WHERE pass == FALSE;", longResponse[9]);
        queriesMap.put("SELECT name FROM marks WHERE mark>60;", longResponse[10]);
        queriesMap.put("DELETE FROM marks WHERE mark<40;", good);
        // queriesMap.put("SELECT * FROM marks;", longResponse[11]);
        queriesMap.put("ALTER TABLE marks ADD age;", good);
        // queriesMap.put("SELECT * FROM marks;", longResponse[12]);
        queriesMap.put("UPDATE marks SET age = 35 WHERE name == 'Simon';", good);
        // queriesMap.put("SELECT * FROM marks;", longResponse[13]);
        queriesMap.put("ALTER TABLE marks DROP pass;", good);
        // queriesMap.put("SELECT * FROM marks;", longResponse[14]);
        queriesMap.put("SELECT * FROM marks", longResponse[15]);
        // Assuming there is NOT a table called “crew” in the database
        queriesMap.put("SELECT * FROM crew;", longResponse[16]);
        // Assuming there is NOT an attribute called “height” in the table
        queriesMap.put("SELECT height FROM marks WHERE name == 'Chris';", longResponse[17]);
        queriesMap.put("DROP TABLE marks;", good);
        queriesMap.put("DROP DATABASE markbook;", good);
    }

    void addEntry(String query, String response) {
    }

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
        setupArrays();
    }

    // Random name generator - useful for testing "bare earth" queriesMap (i.e. where tables don't previously exist)
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

    // very basic tests of functionality
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

    // better tests
    @ParameterizedTest
    @ValueSource(strings = {"!", ".", "", "⛈", "🤠", "!a", ",1", "⛈t", "🤠5"})
    void testPlainTextTokens(String name) {
        String randomName = generateRandomName();
        String response = sendCommandToServer("CREATE DATABASE " + name + ";");
        assertTrue(response.contains("[ERROR] found during lexing"), "query to create database includes non plain text for databaseName");
        response = sendCommandToServer("CREATE TABLE " + randomName + "(name, " + name + ", pass);");
        assertTrue(response.contains("[ERROR] found during lexing"), "query to create table includes non plain text for attributeName");
        sendCommandToServer("INSERT INTO " + randomName + "VALUES ('Simon', 65, TRUE);");
        response = sendCommandToServer("DELETE FROM " + randomName + "WHERE" + name + " == 'Simon' OR mark == 38;");
        assertTrue(response.contains("[ERROR] found during lexing"), "query to delete from table contains non plain text for attributeName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"!", ".", "", "⛈", "🤠", "!a", ",1", "⛈t", "🤠5"})
    void testValueTokens(String name) {
        String response = sendCommandToServer("UPDATE marks SET mark = 98 WHERE name == " + name + ";");
        assertTrue(response.contains("[ERROR] found during lexing"), "value token not set properly due to bad input");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "createCommand databaseSelector databaseName terminator terminator",
            "joinCommand tableOneName and tableTwoName attributeOneName and attributeTwoName terminator"})
    void testWrongqueriesMap(String query) {
        sendCommandToServer(query);
    }

    // transcript
    @Test
    void testOne() {
        String randomName = generateRandomName();
        String query = "CREATE DATABASE " + randomName + ";";
        String response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "USE " + randomName + ";";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "CREATE TABLE marks (name, mark, pass);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "INSERT INTO marks VALUES ('Simon', 65, TRUE);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "INSERT INTO marks VALUES ('Sion', 55, TRUE);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "INSERT INTO marks VALUES ('Rob', 35, FALSE);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "INSERT INTO marks VALUES ('Chris', 20, FALSE);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "SELECT * FROM marks;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[0], response);

        query = "SELECT * FROM marks WHERE name != 'Sion';";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[1], response);

        query = "SELECT * FROM marks WHERE pass == TRUE;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[2], response);

        // setup coursework table
        sendCommandToServer("CREATE TABLE coursework (task, submission);");
        sendCommandToServer("INSERT INTO coursework VALUES ('OXO', 3);");
        sendCommandToServer("INSERT INTO coursework VALUES ('DB', 1);");
        sendCommandToServer("INSERT INTO coursework VALUES ('OXO', 4);");
        sendCommandToServer("INSERT INTO coursework VALUES ('STAG', 2);");

        query = "SELECT * FROM coursework;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[3], response);

        query = "JOIN coursework AND marks ON submission AND id;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[4], response);

        query = "UPDATE marks SET mark = 38 WHERE name == 'Chris';";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);

        query = "SELECT * FROM marks WHERE name == 'Chris';";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[5], response);

        query = "DELETE FROM marks WHERE name == 'Sion';";
        response = sendCommandToServer(query);
        Assertions.assertEquals(good, response);
        
        query = "SELECT * FROM marks;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[6], response);

        query = "SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[7], response);

        query = "SELECT * FROM marks WHERE name LIKE 'i';";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[8], response);

        query = "SELECT id FROM marks WHERE pass == FALSE;";
        response = sendCommandToServer(query);
        Assertions.assertEquals(longResponse[9], response);

        // query = "SELECT name FROM marks WHERE mark>60;";
        // response = sendCommandToServer(query);
        // Assertions.assertEquals(longResponse[10], response);
        //
        // query = "DELETE FROM marks WHERE mark<40;";
        // response = sendCommandToServer(query);
        // Assertions.assertEquals(good, response);
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //





    }

}

