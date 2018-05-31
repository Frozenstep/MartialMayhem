package com.example.andrew.martialmayhem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

public class Ninja extends Enemy {
    //size of the screen
    private GameView view;
    private final int NUMBEROFFRAMES=10;
    private final int LEFTATTACK=9, RIGHTATTACK=10;
    private Bitmap[] frames;
    private int currentFrame=0;
    Ninja(Context context, int width, int height, GameView View){

        this.width=width;
        this.height=height;

        STATE=0;
        this.view=View;
        paint = new Paint();
        frames = new Bitmap[NUMBEROFFRAMES];
        frames[0]= BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjarun2);
        frames[1]= BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjarun3);
        frames[2]= BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjarun4);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        frames[3] = Bitmap.createBitmap(frames[0], 0, 0, frames[0].getWidth(), frames[0].getHeight(), matrix, false);
        frames[4] = Bitmap.createBitmap(frames[1], 0, 0, frames[1].getWidth(), frames[1].getHeight(), matrix, false);
        frames[5] = Bitmap.createBitmap(frames[2], 0, 0, frames[2].getWidth(), frames[2].getHeight(), matrix, false);
        frames[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjakick);
        frames[7] = Bitmap.createBitmap(frames[6], 0, 0, frames[0].getWidth(), frames[0].getHeight(), matrix, false);
        frames[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninjadie1);
        frames[9] = Bitmap.createBitmap(frames[8], 0, 0, frames[8].getWidth(), frames[8].getHeight(), matrix, false);
    }
    public void drawSelf(Canvas canvas, int playerAction){
        switch(STATE) {
            case LEFT:
                if(playerAction==3 && x > ((width/2) - 600)){
                    this.STATE=LEFTDYING;
                    break;
                }
                else if(x>((width/2)-400)){
                    view.hit();
                    view.getPlayer().hurt();
                    changeState(LEFTATTACK);
                    currentFrame=0;
                    break;
                }
                x=x+2;
                canvas.drawBitmap(frames[(++currentFrame)/10], x, y, null);
                if(currentFrame==59){
                    currentFrame=29;
                }
                break;
            case RIGHT:
                if(playerAction==2 && x < ((width/2)+200)){
                    this.STATE=RIGHTDYING;
                    break;
                }
                else if(x<((width/2))){
                    view.hit();
                    view.getPlayer().hurt();
                    changeState(RIGHTATTACK);
                    currentFrame=0;
                    break;
                }
                x=x-2;
                canvas.drawBitmap(frames[(++currentFrame)/10], x, y, null);
                if(currentFrame==29){
                    currentFrame=-1;
                }
                break;
            case UPPERLEFT:

                break;
            case UPPERRIGHT:
                break;
            case LEFTDYING:
                x=x-5;
                canvas.drawBitmap(frames[9], x, y, paint);
                paint.setAlpha(paint.getAlpha()-10);
                if(paint.getAlpha()<10){
                    this.changeState(INACTIVE);
                    paint.setAlpha(100);
                }
                break;
            case RIGHTDYING:
                x=x+5;
                canvas.drawBitmap(frames[8], x, y, paint);
                paint.setAlpha(paint.getAlpha()-10);
                if(paint.getAlpha()<10){
                    this.changeState(INACTIVE);
                    paint.setAlpha(100);
                }
                break;
            case LEFTATTACK:
                x=x+2;
                canvas.drawBitmap(frames[7], x, y, paint);
                ++currentFrame;
                if(currentFrame>30)
                {
                    paint.setAlpha(paint.getAlpha()-10);
                    if(paint.getAlpha()<10){
                        this.changeState(INACTIVE);
                        paint.setAlpha(100);
                    }
                }
                break;
            case RIGHTATTACK:
                x=x-2;
                canvas.drawBitmap(frames[6], x, y, paint);
                ++currentFrame;
                if(currentFrame>30)
                {
                    paint.setAlpha(paint.getAlpha()-10);
                    if(paint.getAlpha()<10){
                        this.changeState(INACTIVE);
                        paint.setAlpha(100);
                    }
                }
                break;
        }

    }
    public int getState(){
        return STATE;
    }
    public void spawn(){
        Random rand = new Random();
        STATE = (rand.nextInt(2) + 1);
        if(STATE==LEFT){
            x=-400;
            y=height-400;
        }
        else if(STATE==RIGHT){
            x=width;
            y=height-400;
        }
        else if(STATE==UPPERLEFT){
            x=-400;
            y=height-400;
        }
        else if(STATE==UPPERRIGHT){
            x=width;
            y=height-400;
        }
    }
    public String getType(){
        return "Ninja";
    }

}
