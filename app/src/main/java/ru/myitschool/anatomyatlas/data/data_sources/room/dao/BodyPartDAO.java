package ru.myitschool.anatomyatlas.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.myitschool.anatomyatlas.data.data_sources.room.entities.BodyPartEntity;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

@Dao
public interface BodyPartDAO {
    @Query("select * from BodyPartEntity")
    LiveData<List<BodyPartEntity>> getAllBodyParts();
    @Insert
    void addBodyPart(BodyPartEntity bodyPart);
    @Query("Delete from BodyPartEntity")
    void clear();
    @Query("select * from BodyPartEntity where name = :name")
    LiveData<BodyPartEntity> getBodyPartByName(String name);
    @Query("update BodyPartEntity set isOpened = \"true\" where name = :name")
    void setOpened(String name);
    @Query("select * from BodyPartEntity where isOpened = \"true\"")
    LiveData<List<BodyPartEntity>> getOpenedBodyParts();
}
