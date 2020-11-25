package com.example.ninemensmorris.View;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ninemensmorris.DB.GameItemDao;
import com.example.ninemensmorris.Model.GameItem;
import com.example.ninemensmorris.Model.NineMenMorrisRules;

import java.util.List;

public class ViewModel extends AndroidViewModel
{
    private GameItem game;
    private List<GameItem> savedGames;

    public ViewModel(@NonNull Application application)
    {
        super(application);
    }

    public void newGame()
    {
        game = new GameItem("name", "#DFFF00");
    }

    public boolean isValid(int To,int From,int color)
    {
        return game.getModel().legalMove(To, From, color);
    }

    public boolean remove(int to, int color)
    {
        return  game.getModel().remove(to, color);
    }

    public boolean remove(int to)
    {
        return  game.getModel().remove(to);
    }

    public boolean win(int color)
    {
        return  game.getModel().win(color);
    }

    public int board(int from)
    {
        return  game.getModel().board(from);
    }
    public int getBluemarker() {
        return  game.getModel().getBluemarker();
    }

    public int getRedmarker() {
        return  game.getModel().getRedmarker();
    }

    public String getBackgroundColor()
    {
        return game.getBackgroundColor();
    }

    public void setBackgroundColor(String color)
    {
        game.setBackgroundColor(color);
    }

    private void getSavedGamesAsync()
    {

    }

    private static class getSavedGames extends AsyncTask<Void, Void, List<String>>
    {

        private GameItemDao mAsyncTaskDao;
        getSavedGames() {
        }


        @Override
        protected List<String> doInBackground(Void... voids)
        {
            return mAsyncTaskDao.getSavedPlaces();
        }

        @Override
        protected void onPostExecute(List<String> strings)
        {
            
        }
    }
}
