package edu.uob;

public class Token {
    String name, tokenType;
    Integer position;

    Token (String name, Integer position) {
        this.name = name;
        this.position = position;
        tokenType = null;
    }

    void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    String getTokenType() {
        return tokenType;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
