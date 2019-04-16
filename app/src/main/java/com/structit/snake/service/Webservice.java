package com.structit.snake.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.structit.snake.Adapter.ScoreboardAdapter;
import com.structit.snake.MainActivity;
import com.structit.snake.R;
import com.structit.snake.Repo.Repo;
import com.structit.snake.Repo.RepoScoreboard;
import com.structit.snake.ScoreboardActivity;
import com.structit.snake.utils.OnEventListener;
import com.structit.snake.utils.SingletonScoreboard;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

public class Webservice extends Service {

    private final int NOTIFICATION_CHANNEL_ID = 101;
    private final String NOTIFICATION_CHANNEL_NAME = "Channel name";
    ListView mListView;

    ArrayAdapter<RepoScoreboard> adapter;

    private List<RepoScoreboard> allScore = new ArrayList<RepoScoreboard>();




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        if(VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Integer.toString(NOTIFICATION_CHANNEL_ID), NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification.Builder notificationBuilder = new Notification.Builder(this, Integer.toString(NOTIFICATION_CHANNEL_ID));
            Notification notification = notificationBuilder.build();
            startForeground(NOTIFICATION_CHANNEL_ID, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("webservice", "onStartCommand");
        //LoginTask loginTask = new LoginTask(this);
        LoginTask requestTask = new LoginTask(this, new OnEventListener() {
            @Override
            public void onResponse(Document document) {
                notifyLogin(document);
            }
        });
        try {
            URL url = new URL("http://snake.struct-it.fr/login.php?user=snake&pwd=test");
            //loginTask.execute(url);
            requestTask.execute(url);

        } catch (Exception ex) {

        }


        return START_STICKY;
    }

    public void notifyLogin(Document mDocument) {

        mDocument.normalizeDocument();
        Element root = mDocument.getDocumentElement();


        Log.d("webservice", root.getAttribute("id"));
        Log.d("webservice", root.getAttribute("url"));

        this.getAllScore();


    }

    public void getAllScore() {
        //ScoreTask scoreTask = new ScoreTask(this);
        LoginTask requestTask = new LoginTask(this, new OnEventListener() {
            @Override
            public void onResponse(Document document) {
                notifyScore(document);
            }
        });
        try {
            URL url = new URL("http://snake.struct-it.fr/score?list");
            requestTask.execute(url);

        } catch (Exception ex) {

        }
    }

    public void notifyScore(Document mDocument) {

        mDocument.normalizeDocument();
        Element root = mDocument.getDocumentElement();

        Log.d("webservice", "notifyScore");

        mDocument.normalizeDocument();
        NodeList nodes = mDocument.getElementsByTagName("score");
        SingletonScoreboard.getInstance().resetArray();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            //Score score = new Score(element.getAttribute("value"), element.getAttribute("player"));
            RepoScoreboard user = new RepoScoreboard();
            user.setName(String.valueOf(element.getAttribute("player")));;
            user.setScore(String.valueOf(element.getAttribute("value")));;

            SingletonScoreboard.getInstance().addToArray(user);
        }

    }

    public void registerScoreUser(String name, String score) {

        //ScoreTask scoreTask = new ScoreTask(this);
        LoginTask requestTask = new LoginTask(this, new OnEventListener() {
            @Override
            public void onResponse(Document document) {

                getAllScore();
                //notifyScore(document);
            }
        });
        try {
            URL url = new URL("http://snake.struct-it.fr/score?player=" + name + "&value=" + score);
            requestTask.execute(url);

        } catch (Exception ex) {

        }

    }



    public void notifyScoreBoard(Boolean success, Document mDocument) {



    }



    }