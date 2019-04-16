package com.structit.snake;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.structit.snake.service.LoginTask;
import com.structit.snake.service.Webservice;
import com.structit.snake.utils.OnEventListener;
import com.structit.snake.view.SnakeOnTouchListener;
import com.structit.snake.view.SnakeView;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_LEFT = 2;
    public static final int DIRECTION_RIGHT = 3;
    public static final int DIRECTION_UP = 4;

    private final String LOG_TAG = MainActivity.class.getName();
    private final int POINT_OFFSET = 4;
    private final int REDRAW_INTERVAL_MS = 500;

    private SnakeView mSnakeView;
    private RedrawHandler mHandler;
    private Snake mSnake;
    private Point mFood;
    private int mDirectionPlayer;
    private Menu mMenu;
    private boolean mIsStopped;
    public int SCORE = 0;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSnakeView = findViewById(R.id.snake);

        SnakeOnTouchListener listener = new SnakeOnTouchListener(this);


        this.mSnakeView.setOnTouchListener(listener);

        this.mHandler = new RedrawHandler(this);
    }

    @Override
    protected void onStart() {


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE},
                101);

        Intent intent = new Intent(this, Webservice.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
            Log.d("mlk", "START FOR: ");
        } else {
            Log.d("mlk", "START INTT: ");
            startService(intent);
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        Intent intent = new Intent(this, Webservice.class);
        this.mMenu.getItem(0).setIcon(R.drawable.ic_play);
        this.mIsStopped = true;

        stopService(intent);

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.mMenu = menu;
        this.mIsStopped = true;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean isSelected;

        switch (item.getItemId()){
            case R.id.start:

                if(this.mIsStopped == true) {
                    this.mMenu.getItem(0).setIcon(R.drawable.ic_stop);
                    this.mIsStopped = false;

                    newGame();
                } else {
                    this.mMenu.getItem(0).setIcon(R.drawable.ic_play);
                    this.mIsStopped = true;
                }

                isSelected = true;
                break;

            case R.id.scoreboardButton:

                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getApplicationContext(), ScoreboardActivity.class);
                startActivity(myIntent);
                isSelected = super.onOptionsItemSelected(item);
                this.onStop();

                break;

            default:
                isSelected = super.onOptionsItemSelected(item);
                break;
        }

        return isSelected;
    }

    private void newGame() {
        Log.d(LOG_TAG, "Starting new game...");

        this.generateSnake();
        this.generateFood();
        this.mDirectionPlayer = DIRECTION_NONE;

        this.mHandler.setInterval(REDRAW_INTERVAL_MS);
        this.mHandler.request();
    }

    private void generateSnake() {
        Random random = new Random();

        this.mSnake = new Snake(POINT_OFFSET +
                random.nextInt(this.mSnakeView.getNbTileX() - 2*POINT_OFFSET),
                POINT_OFFSET +
                        random.nextInt(this.mSnakeView.getNbTileY() - 2*POINT_OFFSET));
    }

    private void generateFood() {
        Random random = new Random();
        Boolean isSnakePosition = false;
        Point food;

        do
        {
            isSnakePosition = false;

            food = new Point(POINT_OFFSET +
                    random.nextInt(this.mSnakeView.getNbTileX() - 2*POINT_OFFSET),
                    POINT_OFFSET +
                            random.nextInt(this.mSnakeView.getNbTileY()- 2*POINT_OFFSET));

            for(int i=0; i < this.mSnake.getLength(); i++)
            {
                if(this.mSnake.getPart(i).equals(food))
                {
                    isSnakePosition = true;
                    break;
                }
                //Else do nothing
            }
        }
        while (isSnakePosition);

        this.mFood = food;
    }

    public void update() {
        switch (this.mDirectionPlayer)
        {
            case DIRECTION_DOWN:
                if (this.mSnake.isBaby() ||
                        this.mSnake.getPart(0).getY() ==
                                this.mSnake.getPart(1).getY())
                    this.mSnake.setDirection(DIRECTION_DOWN);
                break;

            case DIRECTION_RIGHT:
                if (this.mSnake.isBaby() ||
                        this.mSnake.getPart(0).getX() ==
                                this.mSnake.getPart(1).getX())
                    this.mSnake.setDirection(DIRECTION_RIGHT);
                break;

            case DIRECTION_LEFT:
                if (this.mSnake.isBaby() ||
                        this.mSnake.getPart(0).getX() ==
                                this.mSnake.getPart(1).getX())
                    this.mSnake.setDirection(DIRECTION_LEFT);
                break;

            case DIRECTION_UP:
                if (this.mSnake.isBaby() ||
                        this.mSnake.getPart(0).getY() ==
                                this.mSnake.getPart(1).getY())
                    this.mSnake.setDirection(DIRECTION_UP);
                break;

            case DIRECTION_NONE:
            default:
                break;
        }

        if (this.mSnake.getPart(0).equals(this.mFood))
        {
            // Augmenter le score
            this.SCORE += 1;
            this.mSnake.Update(true);
            this.generateFood();
        }
        else
        {
            this.mSnake.Update(false);
        }

        if (this.mSnake.getPart(0).getX() < 1 ||
                this.mSnake.getPart(0).getX() > this.mSnakeView.getNbTileX()-2 ||
                this.mSnake.getPart(0).getY() < 1 ||
                this.mSnake.getPart(0).getY() > this.mSnakeView.getNbTileY()-2 ||
                this.mSnake.isBitting())
        {

            // COLLISION
            this.registerScore();
            this.mMenu.getItem(0).setIcon(R.drawable.ic_play);
        }
        else if(this.mIsStopped) {
            //Refresh the canvas

            this.mSnakeView.clearTiles();
            this.mSnakeView.invalidate();
        }
        else
        {
            //Refresh the canvas
            this.mSnakeView.clearTiles();
            this.mSnakeView.updateFood(this.mFood);
            this.mSnakeView.updateSnake(this.mSnake);
            this.mSnakeView.invalidate();

            this.mHandler.request();
        }
    }

    public void updateDirectionPlayer(float rawX, float rawY) {
        double x = this.mSnakeView.getTileX(rawX);
        double y = this.mSnakeView.getTileY(rawY);

        switch(this.mSnake.getDirection())
        {
            case DIRECTION_UP:
            case DIRECTION_DOWN:
                if(this.mSnake.getPart(0).getX() > x) {
                    this.mDirectionPlayer = DIRECTION_LEFT;
                } else {
                    this.mDirectionPlayer = DIRECTION_RIGHT;
                }
                break;

            case DIRECTION_LEFT:
            case DIRECTION_RIGHT:
                if(this.mSnake.getPart(0).getY() > y) {
                    this.mDirectionPlayer = DIRECTION_UP;
                } else {
                    this.mDirectionPlayer = DIRECTION_DOWN;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if(permissions.length == 3) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Snake", "Permissions granted");
                        Intent intent = new Intent(this, Webservice.class);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }
                    } else {
                        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Snake", "Wake Lock permission not granted");
                        }
                        if(grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Snake", "Internet permission not granted");
                        }
                        if(grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Snake", "Access network permission not granted");
                        }
                    }
                }
                break;
            default: break;
        }
    }

    public void registerScore() {
        final Webservice webservice = new Webservice();

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        final TextView userScore = (TextView) promptsView
                .findViewById(R.id.scoreView);

        userScore.setText("Votre score est de : " + SCORE);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Toast.makeText(MainActivity.this, "Score enregistré !!", Toast.LENGTH_LONG).show();
                                webservice.registerScoreUser(String.valueOf(userInput.getText()), String.valueOf(SCORE));

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
