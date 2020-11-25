package com.example.ninemensmorris.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_item_table")
public class GameItem
{
    @ColumnInfo(name = "name")
    @PrimaryKey
    @NonNull
    private String name;
    @NonNull
    private String backgroundColor;
    @Ignore
    private NineMenMorrisRules model;

    public void setModel(NineMenMorrisRules model) {
        this.model = model;
    }

    public GameItem(String name, String backgroundColor)
    {
        this.model = new NineMenMorrisRules();
        this.backgroundColor = backgroundColor;
        this.name = name;
    }

    public GameItem()
    {
        this.model = new NineMenMorrisRules();
        this.backgroundColor = "#DFFF00";
        this.name = "name";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NineMenMorrisRules getModel() {
        return model;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
