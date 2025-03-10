package edu.uob;

import java.util.ArrayList;

public interface DBConditional {
        abstract DBConditional parse(ArrayList<TokenBank> tokenBanks);
        abstract boolean executeCommand(ArrayList<String> arguments);
}
