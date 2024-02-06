package com.example.snake;

public class GameModal {
    private String playerName;
    private float points;
    private long startGameTime;
    private long duration;
    private int id;

    public String getPlayerName() {
        return playerName;
    }

    public float getPoints() {
        return points;
    }

    public long getStartGameTime() {
        return startGameTime;
    }

    public long getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }

    public GameModal(String playerName, float points, long startGameTime, long duration) {
        this.playerName = playerName;
        this.points = points;
        this.startGameTime = startGameTime;
        this.duration = duration;
    }

}