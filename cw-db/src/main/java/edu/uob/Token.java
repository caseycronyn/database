package edu.uob;

public class Token {
    private String name, tokenType;
    private Integer position;

    Token (String name, Integer position) {
        this.name = name;
        this.position = position;
        tokenType = null;
        // dataType = null;
    }

    void setDataType() {
        // this.dataType = dataType;
    }

    void getDataType() {
        switch (tokenType) {
            case "integerLiteral":
                returnInt();

        }
    }

    int returnInt() {
        return Integer.parseInt(name);
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

    void nameToUpperCase() {
        name = name.toUpperCase();
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }
}
