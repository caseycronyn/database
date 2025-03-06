package edu.uob;

import java.util.ArrayList;

/*
Parser builds subclass of
DBcmd as it parses. This is
returned to DBServer which
passes a reference to itself
as an argument to the method
used to execute the command.
The context given by parsing
        the BNF, combined with Token
types facilitates error
handling and command building
 */
public class Parser {
    public DBCmd parse(ArrayList<String> tokens) {

        return null;
    }
}
