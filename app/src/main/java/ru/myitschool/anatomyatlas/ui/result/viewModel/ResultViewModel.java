package ru.myitschool.anatomyatlas.ui.result.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.myitschool.anatomyatlas.data.models.Money;
import ru.myitschool.anatomyatlas.data.repositories.MoneyRepository;

public class ResultViewModel extends ViewModel {
    private MoneyRepository repository;
    private LiveData<Money> moneyContainer;
    private boolean added = false;

    public ResultViewModel(MoneyRepository moneyRepository){
        repository = moneyRepository;
        moneyContainer = repository.getMoney();
    }

    public LiveData<Money> getMoneyContainer() {
        return moneyContainer;
    }

    public void addMoney(int money){
        if (!added) {
            added = true;
            if (moneyContainer.getValue() != null) {
                repository.updateMoney(new Money(moneyContainer.getValue().getValue() + money));
            }
        }
    }

}
