package ru.myitschool.anatomyatlas.ui.quiz.viewModel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class QuizViewModel extends ViewModel {
    private final BodyPartRepository bodyPartRepository;
    private MutableLiveData<List<BodyPart>> bodyPartContainer;
    private boolean shuffled = false;
    private int score = 0;
    private int counter = 0;
    private MutableLiveData<Integer> progress =new MutableLiveData<>(0);
    private Timer timer;
    private boolean pause = false;
    private boolean started = false;
    private int progressMax;

    public QuizViewModel(BodyPartRepository bodyPartRepository, int progressMax) {
        this.progressMax = progressMax;
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
        progress.setValue(progressMax);
        bodyPartContainer.setValue(newList);
        return names_equals;
    }
    public MutableLiveData<Integer> getProgress(){
        return progress;
    }
    public void startTimer(){
        if (!started){
            started = true;
            progress.setValue(progressMax);
            Handler handler = new Handler(Looper.getMainLooper());
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (progress.getValue() == 0) {
                        handler.post(() -> select(null));
                    }
                    if (!pause) {
                        handler.post(() -> progress.setValue(progress.getValue() - 1));
                    }
                }
            }, 0, 30);
        }
    }

    public int getScore(){
        return score;
    }
    public int getCounter(){
        return counter;
    }
    public void setPause(boolean pause){
        this.pause = pause;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i("VIEWMODEL", "OnCleared");
        timer.cancel();
    }
}
