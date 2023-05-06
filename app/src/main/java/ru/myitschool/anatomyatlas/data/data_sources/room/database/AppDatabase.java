package ru.myitschool.anatomyatlas.data.data_sources.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.myitschool.anatomyatlas.data.data_sources.room.dao.BodyPartDAO;
import ru.myitschool.anatomyatlas.data.data_sources.room.dao.MoneyDAO;
import ru.myitschool.anatomyatlas.data.data_sources.room.entities.BodyPartEntity;
import ru.myitschool.anatomyatlas.data.data_sources.room.entities.MoneyEntity;

@Database(entities = {BodyPartEntity.class, MoneyEntity.class}, version = 1)
abstract public class AppDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppDatabase INSTANCE;


    public abstract BodyPartDAO bodyPartDAO();
    public abstract MoneyDAO moneyDAO();

    public static AppDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            "app_database")
                            .createFromAsset("body_parts.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
