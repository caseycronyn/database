package edu.uob;

import java.util.ArrayList;

public class ComparatorConditional extends FindConditional {

    public FindConditional parse(ArrayList<String> arguments) {
        Integer operatorInt = (arguments.size() / 2);
        String operator = arguments.get(operatorInt);

        if (operator.equals(" LIKE ")) {
            //return to later
        }
        else if (operator.equals("==")) {
//            return to later;
        }
        else {
            FindConditional numericalComparison = new NumericalComparison();
            numericalComparison.parse(arguments);
        }

    }
}
