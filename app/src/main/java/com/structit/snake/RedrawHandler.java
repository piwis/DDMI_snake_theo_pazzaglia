package com.structit.snake;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RedrawHandler extends Handler {
    private final String LOG_TAG = RedrawHandler.class.getName();

    private MainActivity mActivity;
    private int mInterval;

    public RedrawHandler(MainActivity activity) {
        this.mActivity = activity;
        this.mInterval = -1;
    }

    public void setInterval(int interval) {
        this.mInterval = interval;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(LOG_TAG, "Handling request...");

        this.mActivity.update();
    }

    public void request() {
        Log.d(LOG_TAG, "Requesting handler...");

        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), this.mInterval);
    }
}
