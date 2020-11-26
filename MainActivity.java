package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ninemensmorris.Model.NineMenMorrisRules;
import com.example.ninemensmorris.RecylerView.Adapter;
import com.example.ninemensmorris.View.ViewController;
import com.example.ninemensmorris.View.ViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ViewModel vm;
    private RecyclerView recyclerView;
    private Adapter adapter;
    public final static String EXTRA_REPLY = "com.example.android.ninemensmorris.extra.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new Adapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vm =  ViewModelProviders.of(this).get(ViewModel.class);
        vm.newGame();
        vm.save();

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
                        }
                    }
                }
                adapter.setGameItemList(nineMenMorrisRules);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        vm.getSavedGamesAsync();
    }

    public void newGame(View view)
    {
        vm.newGame();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_REPLY, "newGame");
        startActivity(intent);
    }
}