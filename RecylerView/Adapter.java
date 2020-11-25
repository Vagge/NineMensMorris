package com.example.ninemensmorris.RecylerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ninemensmorris.GameActivity;
import com.example.ninemensmorris.Model.GameItem;
import com.example.ninemensmorris.Model.NineMenMorrisRules;
import com.example.ninemensmorris.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder>
{
    private List<NineMenMorrisRules> gameItemList;
    private LayoutInflater inflater;
    private Activity activity;

    public Adapter(Context context, Activity activity)
    {
        inflater = LayoutInflater.from(context);
        this.activity = activity;
    }
    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflater.inflate(R.layout.saved_game_item, parent, false);
        return new AdapterViewHolder(mItemView, this);
    }

    public void setGameItemList(List<NineMenMorrisRules> gameItemList) {
        this.gameItemList = gameItemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position)
    {
        String current = gameItemList.get(position).getName();
        holder.name.setText(current);
    }

    @Override
    public int getItemCount() {
        if(gameItemList!=null)
        {
            return gameItemList.size();
        }
        return 0;
    }

    class AdapterViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener
    {
        final Adapter adapter;
        public final TextView name;
        public final static String EXTRA_REPLY = "com.example.android.ninemensmorris.extra.MESSAGE";
        public AdapterViewHolder(@NonNull View itemView, Adapter adapter) {
            super(itemView);
            this.adapter = adapter;
            name = itemView.findViewById(R.id.game_name);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(activity, GameActivity.class);
            intent.putExtra(EXTRA_REPLY, gameItemList.get(getLayoutPosition()).getName());
            activity.startActivity(intent);
        }
    }
}
