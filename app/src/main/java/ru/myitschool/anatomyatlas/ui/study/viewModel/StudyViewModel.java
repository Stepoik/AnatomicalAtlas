package ru.myitschool.anatomyatlas.ui.study.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class StudyViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedIdContainer = new MutableLiveData<>();
    private final MutableLiveData<String> informationContainer = new MutableLiveData<>();
    private final BodyPartRepository repository;
    public StudyViewModel(BodyPartRepository repository){
        this.repository = repository;
    }
    public void setSelected(int id, String name){
        if (selectedIdContainer.getValue() != null && id == selectedIdContainer.getValue()){
            selectedIdContainer.setValue(0);
            return;
        }
        selectedIdContainer.setValue(id);
        repository.getBodyPartByName(name).observeForever(bodyPart ->{
                if (bodyPart != null){
                    informationContainer.setValue(bodyPart.getInformation());
                }
        });
    }
    public MutableLiveData<Integer> getSelectedIdContainer(){
        return selectedIdContainer;
    }
    public MutableLiveData<String> getInformationContainer(){return informationContainer;}
}
