import java.util.HashMap;

class ExampleTranscript {
String[] longResponse = {
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
            2 Sion 55 TRUE
            3 Rob 35 FALSE
            4 Chris 20 FALSE
        """,
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
            3 Rob 35 FALSE
            4 Chris 20 FALSE
        """,
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
            2 Sion 55 TRUE
        """,
        """
            [OK]
            id task submission
            1 OXO 3
            2 DB 1
            3 OXO 4
            4 STAG 2
        """,
        """
            [OK]
            id coursework.task marks.name marks.mark marks.pass
            1 OXO Rob 35 FALSE
            2 DB Simon 65 TRUE
            3 OXO Chris 20 FALSE
            4 STAG Sion 55 TRUE
        """,
        """
            [OK]
            id name mark pass
            4 Chris 38 FALSE
        """,
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
            3 Rob 35 FALSE
            4 Chris 38 FALSE
        """,
        """
            [OK]
            id name mark pass
            4 Chris 38 FALSE
        """,
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
            4 Chris 38 FALSE
        """,
        """
            [OK]
            id
            3
            4
        """,
        """
            [OK]
            name
            Simon
        """,
        """
            [OK]
            id name mark pass
            1 Simon 65 TRUE
        """,
        """
            [OK]
            id name mark pass age
            1 Simon 65 TRUE
        """,
        """
            [OK]
            id name mark pass age
            1 Simon 65 TRUE 35
        """,
        """
            [OK]
            id name mark age
            1 Simon 65 35
        """,
        """
            [ERROR]: Semi colon missing at end of line
        """,
        """
            [ERROR]: Table does not exist
        """,
        """
            [ERROR]: Attribute does not exist 
        """,
}

String good = "[OK]";

// query, response. True = [OK], False = [Error]
List<HashMap<String, String>> queries = new ArrayList<>();

    "CREATE DATABASE markbook;", good,
    "USE markbook;", good,
    "CREATE TABLE marks (name, mark, pass);", good,
    "INSERT INTO marks VALUES ('Simon', 65, TRUE);", good,
    "INSERT INTO marks VALUES ('Sion', 55, TRUE);", good,
    "INSERT INTO marks VALUES ('Rob', 35, FALSE);", good,
    "INSERT INTO marks VALUES ('Chris', 20, FALSE);", good,
    "SELECT * FROM marks;", longResponse[0],
    "SELECT * FROM marks WHERE name != 'Sion';", longResponse[1],
    "SELECT * FROM marks WHERE pass == TRUE;", longResponse[2],
    // Assuming there is a table called “coursework” in the database (and that table has been filled with data)
    "SELECT * FROM coursework;", longResponse[3],
    // For JOINs: discard the ids from the original tables discard the columns that the tables were matched on create a new unique id for each of row of the table produced attribute names are prepended with name of table from which they originated
    "JOIN coursework AND marks ON submission AND id;", longResponse[4],
    "UPDATE marks SET mark = 38 WHERE name == 'Chris';", good,
    "SELECT * FROM marks WHERE name == 'Chris';", longResponse[5],
    "SELECT * FROM marks;", longResponse[6],
    "DELETE FROM marks WHERE name == 'Sion';", good,
    "SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);", longResponse[7],
    "SELECT * FROM marks WHERE name LIKE 'i';", longResponse[8],
    "SELECT id FROM marks WHERE pass == FALSE;", longResponse[9],
    "SELECT name FROM marks WHERE mark>60;", longResponse[10],
    "DELETE FROM marks WHERE mark<40;", good,
    "SELECT * FROM marks;", longResponse[11],
    "ALTER TABLE marks ADD age;", good,
    "SELECT * FROM marks;", longResponse[12],
    "UPDATE marks SET age = 35 WHERE name == 'Simon';", good,
    "SELECT * FROM marks;", longResponse[13],
    "ALTER TABLE marks DROP pass;", good,
    "SELECT * FROM marks;", longResponse[14],
    "SELECT * FROM marks", longResponse[15],
    // Assuming there is NOT a table called “crew” in the database
    "SELECT * FROM crew;", longResponse[16],
    // Assuming there is NOT an attribute called “height” in the table
    "SELECT height FROM marks WHERE name == 'Chris';", longResponse[17],
    "DROP TABLE marks;", good,
    "DROP DATABASE markbook;", good,
}

void addEntry(String query, String response) {
        HashMap<String, String> entry = new HashMap<>();
        entry.put("query", query);
        entry.put("response", response);
        list.add(entry);
}

}