package ru.myitschool.anatomyatlas.data.models;

public class BodyPart {
    private String name;
    private boolean isOpened;
    private String information;
    private String group;

    public String getInformation() {
        return information;
    }

    public BodyPart(String name, boolean isOpened, String information, String group){
        this.name = name;
        this.isOpened = isOpened;
        this.information = information;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public String getGroup() {
        return group;
    }
}
