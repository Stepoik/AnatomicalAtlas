package ru.myitschool.anatomyatlas.data.models;

public class BodyPart {
    private String name;
    public BodyPart(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
