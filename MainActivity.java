package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.example.ninemensmorris.View.ViewController;
import com.example.ninemensmorris.View.ViewModel;

public class MainActivity extends AppCompatActivity
{
    private ViewModel vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vm =  ViewModelProviders.of(this).get(ViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("tag2", vm.getBackgroundColor());
        startGame();
    }

    private void startGame()
    {
        ViewController v = new ViewController(vm, findViewById(R.id.information), this);
    }
}