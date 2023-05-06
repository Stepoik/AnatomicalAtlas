package ru.myitschool.anatomyatlas.data.data_sources.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.util.TableInfo;

import ru.myitschool.anatomyatlas.data.models.BodyPart;

@Entity
public class BodyPartEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo(defaultValue = "0")
    @NonNull
    public boolean isOpened;
    @ColumnInfo
    public String information;
    @ColumnInfo(defaultValue = "Skeleton")
    public String groupName;

    public BodyPartEntity(String name, boolean isOpened, String information, String groupName) {
        this.name = name;
        this.isOpened = isOpened;
        this.information = information;
        this.groupName = groupName;
    }

    public BodyPart toDomainModel(){
        return new BodyPart(name, isOpened, information, groupName);
    }
}
