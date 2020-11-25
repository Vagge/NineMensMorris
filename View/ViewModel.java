package com.example.ninemensmorris.View;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.ninemensmorris.DB.GameItemDB;
import com.example.ninemensmorris.DB.GameItemDao;
import com.example.ninemensmorris.Model.GameItem;
import com.example.ninemensmorris.Model.NineMenMorrisRules;

import java.util.List;

public class ViewModel extends AndroidViewModel
{
    private GameItem game;
    private MutableLiveData<List<GameItem>> savedGames;
    private MutableLiveData<List<NineMenMorrisRules>> savedRules;
    private GameItemDao dao;

    public ViewModel(@NonNull Application application)
    {
        super(application);
        savedGames = new MutableLiveData<>();
        savedRules = new MutableLiveData<>();
        GameItemDB db = GameItemDB.getDatabase(application);
        dao = db.gameItemDao();
        game = new GameItem();
    }

    public void setGame(GameItem game) {
        this.game = game;
    }

    public GameItem getGame() {
        return game;
    }

    public void newGame()
    {
        game = new GameItem("name", "#DFFF00");
    }

    public boolean isValid(int To,int From,int color)
    {
        return game.getModel().legalMove(To, From, color);
    }

    public MutableLiveData<List<NineMenMorrisRules>> getSavedRules() {
        return savedRules;
    }

    public void setSavedRules(MutableLiveData<List<NineMenMorrisRules>> savedRules) {
        this.savedRules = savedRules;
    }

    public MutableLiveData<List<GameItem>> getSavedGames() {
        return savedGames;
    }

    public void setSavedGames(MutableLiveData<List<GameItem>> savedGames) {
        this.savedGames = savedGames;
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

    public void getSavedGamesAsync()
    {
        new getSavedGames(dao, savedGames).execute();
        new getSavedRules(dao, savedRules).execute();
    }

    public void save()
    {
        new save(dao, game).execute();
    }

    private static class getSavedGames extends AsyncTask<Void, Void, List<GameItem>>
    {
        private MutableLiveData<List<GameItem>> savedGames;
        private GameItemDao dao;
        getSavedGames(GameItemDao dao, MutableLiveData<List<GameItem>> savedGames)
        {
            this.savedGames = savedGames;
            this.dao = dao;
        }


        @Override
        protected List<GameItem> doInBackground(Void... voids) {
            return dao.getAllGameItems();
        }

        @Override
        protected void onPostExecute(List<GameItem> gameItems) {
            savedGames.postValue(gameItems);
        }
    }

    private static class save extends AsyncTask<Void, Void, Void>
    {
        private GameItem game;
        private GameItemDao dao;
        save(GameItemDao dao, GameItem game)
        {
            this.game = game;
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                game.getModel().setName(game.getName());
                dao.insert(game);
                dao.insert(game.getModel());
                Log.d("save", "here");
            }
            catch(SQLiteConstraintException e)
            {

            }
            return null;
        }
    }

    private static class getSavedRules extends AsyncTask<Void, Void, List<NineMenMorrisRules>>
    {
        @Override
        protected void onPostExecute(List<NineMenMorrisRules> nineMenMorrisRules) {
            savedRules.postValue(nineMenMorrisRules);
        }

        private MutableLiveData<List<NineMenMorrisRules>> savedRules;
        private GameItemDao dao;
        getSavedRules(GameItemDao dao, MutableLiveData<List<NineMenMorrisRules>> savedRules)
        {
            this.savedRules = savedRules;
            this.dao = dao;
        }


        @Override
        protected List<NineMenMorrisRules> doInBackground(Void... voids) {
            return dao.getAllRuleItems();
        }
    }
}
