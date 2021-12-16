package com.mathquiz.mathquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mathquiz.mathquiz.model.Player;
import com.mathquiz.mathquiz.R;

import java.util.ArrayList;

public class scoreAdapter extends RecyclerView.Adapter<scoreAdapter.MyViewHolder> {

    Context context;
    ArrayList<Player> list;

    public scoreAdapter(Context context, ArrayList<Player> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public scoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.score_card_views,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull scoreAdapter.MyViewHolder holder, int position) {

        Player player = list.get(position);
        holder.playerName.setText(player.getName());
        holder.playerScore.setText(player.getScore().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView playerName, playerScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.idName);
            playerScore = itemView.findViewById(R.id.idScore);

        }
    }

}
