package com.example.snake;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Collections;

public class ViewGames extends AppCompatActivity {
    private ArrayList<GameModal> gameModalArrayList;
    private DBHealper dbHandler;
    private GameRvAdapter gameRvAdapter;
    private RecyclerView gamesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_db);

        gameModalArrayList = new ArrayList<>();
        dbHandler = new DBHealper(ViewGames.this);

        gameModalArrayList = dbHandler.readGames();

        Collections.sort(gameModalArrayList, (o1, o2) -> Float.compare(o2.getPoints(), o1.getPoints()));

        gameRvAdapter = new GameRvAdapter(gameModalArrayList, ViewGames.this);
        gamesRV = findViewById(R.id.game_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewGames.this, RecyclerView.VERTICAL, false);
        gamesRV.setLayoutManager(linearLayoutManager);

        gamesRV.setAdapter(gameRvAdapter);
    }
}