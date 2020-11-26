package com.example.ninemensmorris.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ninemensmorris.Model.GameItem;
import com.example.ninemensmorris.Model.NineMenMorrisRules;

import java.util.List;

@Dao
public interface GameItemDao
{
    @Insert
    void insert(GameItem item);

    @Insert
    void insert(NineMenMorrisRules rules);

    @Query("SELECT * from GAME_ITEM_TABLE")
    List<GameItem> getAllGameItems();

    @Query("SELECT * from rules_table")
    List<NineMenMorrisRules> getAllRuleItems();

    @Query("DELETE FROM game_item_table WHERE name = :name")
    void deleteGame(String name);

    @Query("DELETE FROM rules_table WHERE name = :name")
    void deleteRule(String name);

    @Query("DELETE FROM game_item_table")
    void deleteAllGames();

    @Query("DELETE FROM rules_table")
    void deleteAllRules();
}
