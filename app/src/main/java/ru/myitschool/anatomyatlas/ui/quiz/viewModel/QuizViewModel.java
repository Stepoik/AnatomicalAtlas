package ru.myitschool.anatomyatlas.ui.quiz.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class QuizViewModel extends ViewModel {
    private final BodyPartRepository bodyPartRepository;
    private MutableLiveData<List<BodyPart>> bodyPartContainer;
    private boolean shuffled = false;
    private int score = 0;
    private int counter = 0;

    public QuizViewModel(BodyPartRepository bodyPartRepository) {
        this.bodyPartRepository = bodyPartRepository;
        bodyPartContainer = (MutableLiveData<List<BodyPart>>)bodyPartRepository.getAllBodyParts();
    }

    public LiveData<List<BodyPart>> getBodyPartContainer() {
        return bodyPartContainer;
    }
    public void shuffleData(){
        List<BodyPart> newList = new ArrayList<>();
        List<BodyPart> oldList = bodyPartContainer.getValue();
        Random random = new Random();
        if (oldList != null) {
            shuffled = true;
            int size = oldList.size();
            for (int i = 0; i < size; i++) {
                int index = Math.abs(random.nextInt())%oldList.size();
                newList.add(oldList.get(index));
                oldList.remove(index);
            }
            bodyPartContainer.setValue(newList.subList(0,Math.min(newList.size(),10)));
        }
    }
    public boolean isShuffled(){
        return shuffled;
    }
    public boolean select(String name){
        List<BodyPart> newList = bodyPartContainer.getValue();
        if (newList == null || newList.size() == 0){
            return false;
        }
        counter++;
        String currentName = newList.get(0).getName();
        newList.remove(0);
        boolean names_equals = Objects.equals(name, currentName);
        if (names_equals){
            score++;
        }
        bodyPartContainer.setValue(newList);
        return names_equals;
    }
    public int getScore(){
        return score;
    }
    public int getCounter(){
        return counter;
    }
}
