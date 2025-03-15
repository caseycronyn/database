package edu.uob;

public class Attribute {
    String name;
    String dataType;

    Attribute(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    void setDataType(String dataType) {
        this.dataType = dataType;
    }

    String getDataType() {
        return dataType;
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
