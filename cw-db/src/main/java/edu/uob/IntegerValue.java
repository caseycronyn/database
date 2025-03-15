package edu.uob;

public class IntegerValue extends Value {
    int value;
    Token token;

    IntegerValue(Token token) {
        this.token = token;
        init();
    }
    void init() {
        value = Integer.parseInt(token.getName());
    }
    int getValue() {
        return value;
    }
}
