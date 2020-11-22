package com.example.ninemensmorris.View;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.ninemensmorris.Model.NineMenMorrisRules;

public class ViewModel extends AndroidViewModel
{
    private NineMenMorrisRules model;


    public ViewModel(@NonNull Application application)
    {
        super(application);
        newGame();
    }

    public void newGame()
    {
        model = new NineMenMorrisRules();
    }

    public boolean isValid(int To,int From,int color)
    {
        return model.legalMove(To, From, color);
    }

    public boolean remove(int to, int color)
    {
        return model.remove(to, color);
    }

    public boolean remove(int to)
    {
        return model.remove(to);
    }

    public boolean win(int color)
    {
        return model.win(color);
    }
}
