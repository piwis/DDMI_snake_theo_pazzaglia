package com.structit.snake;

import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> mPartList;
    private int mDirection;

    public Snake(int x, int y) {
        this.mPartList = new LinkedList<Point>();
        this.mDirection = MainActivity.DIRECTION_DOWN;

        Point head = new Point(x, y);
        this.mPartList.add(head);
    }

    public void Update(Boolean isGrowing)
    {
        for (int i = this.mPartList.size() - 1; i >= 0; i--)
        {
            if (i == 0)
            {
                switch (this.mDirection)
                {
                    case MainActivity.DIRECTION_DOWN:
                        this.mPartList.get(i).incY();
                        break;

                    case MainActivity.DIRECTION_LEFT:
                        this.mPartList.get(i).decX();
                        break;

                    case MainActivity.DIRECTION_RIGHT:
                        this.mPartList.get(i).incX();
                        break;

                    case MainActivity.DIRECTION_UP:
                        this.mPartList.get(i).decY();
                        break;

                    default:
                        break;
                }
            }
            else
            {
                this.mPartList.get(i).setX(this.mPartList.get(i-1).getX());
                this.mPartList.get(i).setY(this.mPartList.get(i-1).getY());
            }
        }

        if (isGrowing)
        {
            this.mPartList.add(
                    new Point(this.mPartList.get(this.mPartList.size()-1).getX(),
                            this.mPartList.get(this.mPartList.size()-1).getY()));
        }
        //Else do nothing
    }

    public void setDirection(int direction)
    {
        this.mDirection = direction;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public Boolean isBaby()
    {
        return this.mPartList.size() == 1;
    }

    public Point getPart(int partNumber)
    {
        return this.mPartList.get(partNumber);
    }

    public int getLength()
    {
        return this.mPartList.size();
    }

    public Boolean isBitting()
    {
        Boolean isBitting = false;

        for(int i = 4; i < this.mPartList.size(); i++)
        {
            if(this.mPartList.get(i).equals(this.mPartList.get(0)))
            {
                isBitting = true;
                break;
            }
        }

        return isBitting;
    }
}
