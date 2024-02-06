package com.example.snake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHealper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gamedb";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "games";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "name";

    private static final String POINTS_COL = "points";

    private static final String START_DATE_COL = "start_date";

    private static final String DURATION_COL = "duration_date";
    public DBHealper(@Nullable Context context) {
        super(context, DB_NAME, null,  DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + POINTS_COL + " REAL,"
                + START_DATE_COL + " INTEGER,"
                +  DURATION_COL + " INTEGER)";

        db.execSQL(query);
    }

    public void addNewGame(String playerName, float gamePoints, long startDate, long duration) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, playerName);
        values.put(POINTS_COL, gamePoints);
        values.put(START_DATE_COL, startDate);
        values.put(DURATION_COL, duration);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public ArrayList<GameModal> readGames() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorGames = db.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<GameModal> gameModalArrayList = new ArrayList<>();

        if (cursorGames.moveToFirst()) {
            do {
                gameModalArrayList.add(new GameModal(cursorGames.getString(1),
                        cursorGames.getFloat(2),
                        cursorGames.getLong(3),
                        cursorGames.getLong(4)));
            } while (cursorGames.moveToNext());
        }
        cursorGames.close();
        return gameModalArrayList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
