package com.example.ninemensmorris.DB;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ninemensmorris.Model.GameItem;

public abstract class GameItemDB extends RoomDatabase
{
    public abstract GameItemDao gameItemDao();
    private static GameItemDB INSTANCE;

    public static GameItemDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameItemDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GameItemDB.class, "game_item_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
