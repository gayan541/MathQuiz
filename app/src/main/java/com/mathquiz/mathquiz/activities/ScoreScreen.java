package com.mathquiz.mathquiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mathquiz.mathquiz.model.Player;
import com.mathquiz.mathquiz.R;
import com.mathquiz.mathquiz.adapters.scoreAdapter;

import java.util.ArrayList;

public class ScoreScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    Query dataBase;
    scoreAdapter scoreAdapter;
    ArrayList<Player> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        recyclerView = findViewById(R.id.recycler);
        dataBase = FirebaseDatabase.getInstance().getReference("Player").orderByChild("decs");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        scoreAdapter = new scoreAdapter(this,list);
        recyclerView.setAdapter(scoreAdapter);

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Player player = dataSnapshot.getValue(Player.class);
                    list.add(player);

                }

                scoreAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}