package com.example.ninemensmorris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.ninemensmorris.View.TouchView;
import com.example.ninemensmorris.View.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ImageView mRedChecker;
    private ImageView mBlueChecker;
    private List<ImageView> board;
    private ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vm =  ViewModelProviders.of(this).get(ViewModel.class);
        initPhaseOne();
    }

    private void initPhaseOne()
    {
        TouchView v = new TouchView(vm);
        mRedChecker = findViewById(R.id.red_checker);
        mBlueChecker = findViewById(R.id.blue_checker);
        board = new ArrayList<>();
        for(int i = 1; i < 25; i++)
        {
            board.add(findViewById(getResources().getIdentifier("pos_"+i,"id", this.getPackageName())));
            board.get(i-1).setOnDragListener(v);
            board.get(i-1).setContentDescription(Integer.toString(i));
        }
        mRedChecker.setOnTouchListener(v);
        mBlueChecker.setOnTouchListener(v);
        mRedChecker.setOnDragListener(v);
        mBlueChecker.setOnDragListener(v);
    }
}