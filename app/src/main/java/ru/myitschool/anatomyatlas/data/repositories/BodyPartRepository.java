package ru.myitschool.anatomyatlas.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import ru.myitschool.anatomyatlas.data.data_sources.room.database.AppDatabase;
import ru.myitschool.anatomyatlas.data.data_sources.room.entities.BodyPartEntity;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

public class BodyPartRepository{
    private AppDatabase sourceDatabase;
    public BodyPartRepository(Context context){
        sourceDatabase = AppDatabase.getInstance(context);
    }
    public LiveData<List<BodyPart>> getAllBodyParts(){
        return Transformations.map(sourceDatabase.bodyPartDAO().getAllBodyParts(), (values)->values.stream().map(BodyPartEntity::toDomainModel).collect(Collectors.toList()));
    }
    public void addBodyPart(BodyPart bodyPart){
        AppDatabase.databaseWriteExecutor.execute(()->
                sourceDatabase.bodyPartDAO().addBodyPart(new BodyPartEntity(bodyPart.getName())));
    }
    public void clear(){
        AppDatabase.databaseWriteExecutor.execute(()->sourceDatabase.bodyPartDAO().clear());
    }
}
