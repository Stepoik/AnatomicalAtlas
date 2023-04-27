package ru.myitschool.anatomyatlas.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.myitschool.anatomyatlas.data.data_sources.room.entities.MoneyEntity;
import ru.myitschool.anatomyatlas.data.models.Money;

@Dao
public interface MoneyDAO {
    @Query("select * from MoneyEntity LIMIT 1")
    LiveData<MoneyEntity> getMoney();
    @Insert
    void createMoney(MoneyEntity moneyEntity);
    @Update
    void updateMoney(MoneyEntity moneyEntity);
}
