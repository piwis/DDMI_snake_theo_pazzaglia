package com.structit.snake.utils;

import com.structit.snake.Repo.RepoScoreboard;

import java.util.ArrayList;
import java.util.List;

public class SingletonScoreboard  {

    private static SingletonScoreboard mInstance;
    private List<RepoScoreboard> allScore = new ArrayList<RepoScoreboard>();

    public static SingletonScoreboard getInstance() {
        if(mInstance == null)
            mInstance = new SingletonScoreboard();

        return mInstance;
    }

    private SingletonScoreboard() {

    }
    // retrieve array from anywhere
    public List<RepoScoreboard> getArray() {
        return this.allScore;
    }
    //Add element to array
    public void addToArray(RepoScoreboard value) {
        allScore.add(value);
    }
    public void resetArray() {
        allScore = new ArrayList<RepoScoreboard>();
    }
}
