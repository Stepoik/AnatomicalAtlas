package ru.myitschool.anatomyatlas.data.models;

public class Money {
    private long value;
    public Money(long value){
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
