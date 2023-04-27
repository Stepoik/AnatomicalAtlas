package ru.myitschool.anatomyatlas.data.data_sources.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ru.myitschool.anatomyatlas.data.models.BodyPart;

@Entity
public class BodyPartEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;

    public BodyPartEntity(String name){
        this.name = name;
    }
    public BodyPart toDomainModel(){
        return new BodyPart(name);
    }
}
