package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ninemensmorris.View.PhaseOne;
import com.example.ninemensmorris.View.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vm =  ViewModelProviders.of(this).get(ViewModel.class);
        startGame();
    }

    private void startGame()
    {
        PhaseOne v = new PhaseOne(vm, findViewById(R.id.information), this);
    }
}