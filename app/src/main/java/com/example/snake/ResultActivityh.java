package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivityh extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        Date playdate;
        SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm:ss");
        long startGameTime = getIntent().getLongExtra("startGameTime", 0);
        long endGameTime = getIntent().getLongExtra("endGameTime", 0);
        int result = getIntent().getIntExtra("inGameTime", 0);
        TextView result_txt = findViewById(R.id.result_txt);
        result_txt.setText(String.format(getResources().getString(R.string.result), Float.toString(result)));
        Button restart_btn = findViewById(R.id.restart_btn);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivityh.this, SecondActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button saveToDBBtn = findViewById(R.id.save_to_db_btn);
        EditText playerNameEdit = findViewById(R.id.player_name_edit);
        DBHealper dbHandler = new DBHealper(ResultActivityh.this);

        saveToDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String playerName = playerNameEdit.getText().toString();
                long duration = endGameTime - startGameTime;
                /*playdate = new Date(duration);*/
                if(playerName.isEmpty()) {
                    Toast.makeText(ResultActivityh.this, getResources().getString(R.string.invalid_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewGame(playerName, result, startGameTime, duration);

                Intent i = new Intent(ResultActivityh.this, SecondActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button showDBBtn = findViewById(R.id.show_db_btn);

        showDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivityh.this, ViewGames.class);
                startActivity(i);
            }
        });
    }
}
