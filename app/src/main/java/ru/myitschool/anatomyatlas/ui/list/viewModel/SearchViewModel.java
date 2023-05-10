package ru.myitschool.anatomyatlas.ui.list.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<BodyPart>> bodyPartList;
    private BodyPartRepository repository;

    public SearchViewModel(BodyPartRepository repository){
        this.repository = repository;
        bodyPartList = (MutableLiveData<List<BodyPart>>) repository.searchBodyPartByName("");
    }

    public MutableLiveData<List<BodyPart>> getBodyPartList() {
        return bodyPartList;
    }

    public void search(String name){
        repository.searchBodyPartByName(name).observeForever(bodyParts -> {
            bodyPartList.setValue(bodyParts);
        });
    }
}
