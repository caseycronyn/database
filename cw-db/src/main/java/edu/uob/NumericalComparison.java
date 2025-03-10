package edu.uob;

import java.util.ArrayList;

public class NumericalComparison extends FindConditional {
    public FindConditional parse(ArrayList<String> arguments) {
        operatorInt = (arguments.size() / 2);
        operator = arguments.get(operatorInt);
        return this;
    }

    executeCommand
}
