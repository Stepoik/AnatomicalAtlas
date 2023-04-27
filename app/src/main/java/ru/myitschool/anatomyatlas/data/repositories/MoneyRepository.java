package ru.myitschool.anatomyatlas.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import ru.myitschool.anatomyatlas.data.data_sources.room.database.AppDatabase;
import ru.myitschool.anatomyatlas.data.data_sources.room.entities.MoneyEntity;
import ru.myitschool.anatomyatlas.data.models.Money;

public class MoneyRepository {
    private AppDatabase appDatabase;
    public MoneyRepository(Context context){
        appDatabase = AppDatabase.getInstance(context);
    }
    public LiveData<Money> getMoney(){
        return Transformations.map(appDatabase.moneyDAO().getMoney(), (values)->{
            if (values != null) {
                return values.toDomainModel();
            }
            return null;
        });
    }
    public void createMoney(Money money){
        AppDatabase.databaseWriteExecutor.execute(()->
                appDatabase.moneyDAO().createMoney(new MoneyEntity(money.getValue())));
    }
    public void updateMoney(Money money){
        AppDatabase.databaseWriteExecutor.execute(()->
                appDatabase.moneyDAO().updateMoney(new MoneyEntity(money.getValue())));
    }
}
