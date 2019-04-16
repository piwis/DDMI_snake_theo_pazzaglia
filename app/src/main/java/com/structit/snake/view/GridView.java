package com.structit.snake.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.structit.snake.R;

public abstract class GridView extends View {
    private final String LOG_TAG = GridView.class.getName();
    private final int TILE_DEFAULT_SIZE = 12;
    private final int TILE_EMPTY = -1;

    private final Paint mPaint;
    private Bitmap[] mTileList;
    private int[][] mGrid;

    private static int mTileSize;
    private static int mOffsetX;
    private static int mOffsetY;

    protected static int mNbTileX;
    protected static int mNbTileY;

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mPaint = new Paint();

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.GridView);

        mTileSize = styledAttributes.getDimensionPixelSize(R.styleable.GridView_tileSize,
                TILE_DEFAULT_SIZE);

        Log.d(LOG_TAG, "Tile size: " + mTileSize);

        styledAttributes.recycle();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mPaint = new Paint();

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.GridView);

        mTileSize = styledAttributes.getDimensionPixelSize(R.styleable.GridView_tileSize,
                TILE_DEFAULT_SIZE);

        Log.d(LOG_TAG, "Tile size: " + mTileSize);

        styledAttributes.recycle();
    }


    protected void resetTileList(int nbTiles) {
        mTileList = new Bitmap[nbTiles];
    }

    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);

        mTileList[key] = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d(LOG_TAG, "Drawing...");

        super.onDraw(canvas);

        for (int x = 0; x < mNbTileX; x++) {
            for (int y = 0; y < mNbTileY; y++) {
                if (mGrid[x][y] > TILE_EMPTY) {
                    canvas.drawBitmap(mTileList[mGrid[x][y]],
                            mOffsetX + x * mTileSize,
                            mOffsetY + y * mTileSize,
                            mPaint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mNbTileX = (int) Math.floor(w / mTileSize);
        mNbTileY = (int) Math.floor(h / mTileSize);

        Log.d(LOG_TAG, "Nb tile - X: " + mNbTileX);
        Log.d(LOG_TAG, "Nb tile - Y: " + mNbTileY);

        mOffsetX = ((w - (mTileSize * mNbTileX)) / 2);
        mOffsetY = ((h - (mTileSize * mNbTileY)) / 2);

        mGrid = new int[mNbTileX][mNbTileY];

        clearTiles();
        this.invalidate();
    }

    public void clearTiles() {
        for (int x = 0; x < mNbTileX; x++) {
            for (int y = 0; y < mNbTileY; y++) {
                setTile(TILE_EMPTY, x, y);
            }
        }

        this.updateTiles();
    }

    protected abstract void updateTiles();

    public void setTile(int value, int x, int y) {
        mGrid[x][y] = value;
    }

    public static int getNbTileX() {
        return mNbTileX;
    }

    public static int getNbTileY() {
        return mNbTileY;
    }

    public static double getTileX(float x) {
        return Math.floor((x - mOffsetX) / mTileSize);
    }

    public static double getTileY(float y) {
        return Math.floor((y - mOffsetY) / mTileSize);
    }
}
