package com.example.ninemensmorris.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ninemensmorris.Model.GameItem;

import java.util.List;

@Dao
public interface GameItemDao
{
    @Insert
    void insert(GameItem item);

    @Query("SELECT * from GAME_ITEM_TABLE")
    List<GameItem> getAllItems();

    @Query("DELETE FROM game_item_table WHERE name = :name")
    void delete(String name);
}
