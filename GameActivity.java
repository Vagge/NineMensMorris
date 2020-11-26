package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ninemensmorris.Model.NineMenMorrisRules;
import com.example.ninemensmorris.RecylerView.Adapter;
import com.example.ninemensmorris.View.ViewController;
import com.example.ninemensmorris.View.ViewModel;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    private ViewModel vm;
    private EditText saveName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        String name = intent.getStringExtra("com.example.android.ninemensmorris.extra.MESSAGE");
        saveName = findViewById(R.id.save_game_txt);
        vm =  ViewModelProviders.of(this).get(ViewModel.class);
        vm.newGame();
        vm.save();
        vm.getSavedGamesAsync();
        if(name.equals("newGame"))
        {
            startGame();
        }
        else
        {
            vm.getSavedRules().observe(this, new Observer<List<NineMenMorrisRules>>() {
                @Override
                public void onChanged(List<NineMenMorrisRules> nineMenMorrisRules)
                {
                    for(int i = 0; i<vm.getSavedGames().getValue().size(); i++)
                    {
                        for(int j = 0; j < vm.getSavedRules().getValue().size(); j++)
                        {
                            if(vm.getSavedRules().getValue().get(i).getName().equals(vm.getSavedRules().getValue().get(j).getName()))
                            {
                                vm.getSavedGames().getValue().get(i).setModel(vm.getSavedRules().getValue().get(j));
                                if( vm.getSavedGames().getValue().get(i).getName().equals(name))
                                {
                                    vm.setGame(vm.getSavedGames().getValue().get(i));
                                    startGame();
                                }
                            }
                        }
                    }
                }
            });
        }

    }

    public void startGame()
    {
        ViewController v = new ViewController(vm, this);
    }

    public void save_game(View view)
    {
        vm.getGame().setName(saveName.getText().toString());
        vm.save();
    }
}