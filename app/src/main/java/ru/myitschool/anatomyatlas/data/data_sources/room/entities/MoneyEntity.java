package ru.myitschool.anatomyatlas.data.data_sources.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.models.Money;

@Entity
public class MoneyEntity {
    @PrimaryKey
    public int id = 0;
    @ColumnInfo
    public long value;
    public Money toDomainModel(){
        return new Money(value);
    }
    public MoneyEntity(long value){
        this.value = value;
    }
}
