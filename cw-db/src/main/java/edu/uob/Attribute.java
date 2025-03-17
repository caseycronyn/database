package edu.uob;

public class Attribute {
    String name;
    String dataType;
    int index;

    Attribute(String name, String dataType, int index) {
        this.name = name;
        this.dataType = dataType;
        this.index = index;
    }

    Attribute copy() {
        String newName = new String(name);
        String newDataType = new String(dataType);
        Attribute newAttribute = new Attribute(newName, newDataType, index);
        return newAttribute;
    }

    int getIndex() {
        return index;
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
