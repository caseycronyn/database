package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class FindConditional implements DBConditional {
    Arguments arguments;
    String operator;
    Integer operatorInt;
    String [] booleanOperators = { "AND", "OR" };
    String [] comparators = { "==", ">", "<", ">=", "<=", "!=", " LIKE " };
    List<String> firstArguments;
    List<String> secondArguments;

    public FindConditional parse(ArrayList<String> arguments) {
        operatorInt = (arguments.size() / 2);
        operator = arguments.get(operatorInt);

        if (tokenIsComparator(operator)) {
            ComparatorConditional conditional = new ComparatorConditional();
            return conditional.parse(arguments);
        }
//        if (tokenIsBooleanOperator(operator)) {
//            firstArguments = arguments.subList(0, operatorInt - 1);
//            secondArguments = arguments.subList(operatorInt + 1, arguments.size());
//
//            FindConditional conditionalOne = new FindConditional();
//            FindConditional conditionalTwo = new FindConditional();
//        }
        return null;
    }

    boolean tokenIsBooleanOperator(String token) {
        for (String operator : booleanOperators) {
            if (operator.equals(token)) {
                return true;
            }
        }
        return false;
    }

    boolean tokenIsComparator(String token) {
        for (String comparator : comparators) {
            if (comparator.equals(token)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DBConditional parse(TokenBank tokenBank) {
        return null;
    }

    @Override
    public boolean executeCommand(ArrayList<String> arguments) {
        return false;
    }
}
