package edu.uob;

public class Attribute {
    String name, dataType;
    int index;

    Attribute(String name, String dataType, int index) {
        this.name = name;
        this.dataType = dataType;
        this.index = index;
    }

    Attribute copyAttribute() {
        return new Attribute(name, dataType, index);
    }

    int getIndex() {
        return index;
    }

    String getDataType() {
        return dataType;
    }

    void setDataType(String dataType) {
        this.dataType = dataType;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}