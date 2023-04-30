package ru.myitschool.anatomyatlas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import ru.myitschool.anatomyatlas.databinding.ActivityMainBinding;
import ru.myitschool.anatomyatlas.ui.NavigateFromChild;

public class MainActivity extends AppCompatActivity implements NavigateFromChild {
    private ActivityMainBinding binding;
    private NavController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void navigate(int id) {
        if (controller == null){
            controller = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        }
        controller.navigate(id);
    }
}