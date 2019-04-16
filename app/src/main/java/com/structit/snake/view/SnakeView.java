package com.structit.snake.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;

import com.structit.snake.Point;
import com.structit.snake.R;
import com.structit.snake.Snake;

public class SnakeView extends GridView {
    private final String LOG_TAG = SnakeView.class.getName();

    private final int TILE_WALL = 0;
    private final int TILE_SNAKE_HEAD = 1;
    private final int TILE_SNAKE_PART = 2;
    private final int TILE_FOOD = 3;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView(context);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView(context);
    }

    private void initSnakeView(Context context) {
        Log.d(LOG_TAG, "Initializing...");

        setFocusable(true);

        Resources r = this.getContext().getResources();

        // TILE_FOOD is the last TILE type
        resetTileList(TILE_FOOD + 1);

        loadTile(TILE_WALL, r.getDrawable(R.drawable.wall));
        loadTile(TILE_SNAKE_HEAD, r.getDrawable(R.drawable.snake_head));
        loadTile(TILE_SNAKE_PART, r.getDrawable(R.drawable.snake_part));
        loadTile(TILE_FOOD, r.getDrawable(R.drawable.food));
    }

    @Override
    protected void updateTiles() {
        Log.d(LOG_TAG, "Updating walls...");

        for (int x = 0; x < mNbTileX; x++) {
            setTile(TILE_WALL, x, 0);
            setTile(TILE_WALL, x, mNbTileY - 1);
        }

        for (int y = 0; y < mNbTileY; y++) {
            setTile(TILE_WALL, 0, y);
            setTile(TILE_WALL, mNbTileX - 1, y);
        }
    }

    public void updateSnake(Snake snake) {
        for(int i = 0; i < snake.getLength(); i++) {
            setTile(i==0?TILE_SNAKE_HEAD:TILE_SNAKE_PART,
                    snake.getPart(i).getX(),
                    snake.getPart(i).getY());
        }
    }

    public void updateFood(Point point) {
        setTile(TILE_FOOD, point.getX(), point.getY());
    }
}
