package com.structit.snake.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.structit.snake.MainActivity;

public class SnakeOnTouchListener implements View.OnTouchListener {
    private final String LOG_TAG = SnakeOnTouchListener.class.getName();

    private MainActivity mainActivity;

    public SnakeOnTouchListener(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(LOG_TAG, "X: " + motionEvent.getX());
        Log.d(LOG_TAG, "Y: " + motionEvent.getY());

        this.mainActivity.updateDirectionPlayer(motionEvent.getX(),
                motionEvent.getY());

        return false;
    }
}
