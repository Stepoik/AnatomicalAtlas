package ru.myitschool.anatomyatlas.ui.quiz_start.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.models.Money;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;
import ru.myitschool.anatomyatlas.data.repositories.MoneyRepository;

public class QuizStartViewModel extends ViewModel {
    private BodyPartRepository bodyPartRepository;
    private MoneyRepository moneyRepository;
    private LiveData<List<BodyPart>> bodyPartContainer;
    private LiveData<Money> moneyContainer;
    public QuizStartViewModel(BodyPartRepository repository, MoneyRepository moneyRepository){
        bodyPartRepository = repository;
        this.moneyRepository = moneyRepository;
        bodyPartContainer = bodyPartRepository.getAllBodyParts();
        moneyContainer = moneyRepository.getMoney();
    }
    public LiveData<List<BodyPart>> getBodyParts(){
        return bodyPartContainer;
    }
    public boolean buyBodyPart(String name){
        if (moneyContainer.getValue() == null){
            return false;
        }
        if (moneyContainer.getValue().getValue() >= 10) {
            moneyRepository.updateMoney(new Money(moneyContainer.getValue().getValue()-10));
            bodyPartRepository.addBodyPart(new BodyPart(name));
            return true;
        }
        return false;
    }
    public void clear(){
        bodyPartRepository.clear();
    }
    public boolean isAlreadyBought(String value){
        if (bodyPartContainer.getValue() != null){
            return (bodyPartContainer.getValue().stream().anyMatch(c->c.getName().equals(value)));
        }
        return false;
    }
    public LiveData<Money> getMoneyContainer(){
        return moneyContainer;
    }
    public void createMoney(){
        moneyRepository.createMoney(new Money(50));
    }
}
