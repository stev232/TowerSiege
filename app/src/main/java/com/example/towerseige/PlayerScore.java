package com.example.towerseige;

public class PlayerScore
{
    private int points;

    public PlayerScore(int points)
    {
        this.points+=points;
    }

    public int getPoints()
    {
        return this.points;
    }
}
