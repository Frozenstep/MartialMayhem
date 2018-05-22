package com.example.andrew.martialmayhem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Enemy {
    //TODO: Make everything protected, make Enemy an abstract class so it's easy to make multiple enemy types that inherit from it.
    private int x, y, id, rotation=0, speed=4;
    private Matrix rotateMatrix;
    private Bitmap star;
    private Bitmap rotatedStar;
    private final int PLAYERTOPRIGHT=1;
    private final int PLAYERBOTTOMRIGHT=2;
    private final int PLAYERBOTTOMLEFT=3;
    private final int PLAYERTOPLEFT=4;
    private final int DEAD=0, LEFTALIVE=1, RIGHTALIVE=2, UPPERLEFT=3, UPPERRIGHT=4, DYING=5;
    private Paint paint;
    private int STATE;
    //size of the screen
    private int width, height;
    private GameView view;
    Enemy(Bitmap input, int id, int width, int height, GameView View){
        this.id=id;
        star=input;
        this.width=width;
        this.height=height;
        rotateMatrix = new Matrix();
        rotateMatrix.postRotate(45, star.getWidth()/2, star.getHeight()/2);
        rotatedStar = Bitmap.createBitmap(input , 0, 0, input.getWidth(), input.getHeight(), rotateMatrix, false);
        rotatedStar.setHasAlpha(true);
        STATE=0;
        this.view=View;
        paint = new Paint();
    }
    //called every time GameView.draw is called. Requires the canvas, and an int that represents what the player has done in the current frame
    public void drawSelf(Canvas canvas, int playerAction){

        if(STATE!=DEAD) {
            //first a quick check to see the player attacked, and if so, check if this enemy is within range. If we are, we go into the dying state.
            if(playerAction==PLAYERBOTTOMRIGHT && x<width/2+220 && x > width/2+20 && STATE==RIGHTALIVE){
                changeState(DYING);
            }
            else if(playerAction==PLAYERBOTTOMLEFT && x<width/2-140 && x > width/2-340 && STATE==LEFTALIVE){
                changeState(DYING);
            }
            else if(playerAction==PLAYERTOPLEFT && x<width/2-140 && x > width/2-340 && STATE==UPPERLEFT){
                changeState(DYING);
            }
            else if(playerAction==PLAYERTOPRIGHT && x<width/2+220 && x > width/2+20 && STATE==UPPERRIGHT){
                changeState(DYING);
            }
            //this isn't complete, but since the only enemy right now is a shuriken, we always gotta spin.
            if (rotation < 10) {
                canvas.drawBitmap(star, x, y, paint);
                ++rotation;
            } else {
                canvas.drawBitmap(rotatedStar, x - 20, y - 20, paint);
                ++rotation;
            }
            if (rotation > 20) {
                rotation = 0;
            }
            //quick series of checks to keep the enemy moving towards the player, or finish it's dying animation.
            if (STATE==LEFTALIVE){
                x+=(speed+1);
            }
            else if (STATE==RIGHTALIVE){
                x-=(speed+1);
            }
            else if (STATE==UPPERRIGHT){
                x-=speed;
                y+=speed;
            }
            else if(STATE==UPPERLEFT){
                x+=speed;
                y+=speed;
            }
            else if(STATE==DYING){
                y-=2;
                x+=2;
                paint.setAlpha(paint.getAlpha()-10);
                if(paint.getAlpha()<10){
                    changeState(DEAD);
                }
            }
            //if enemy isn't dying and manges to touch the player, hurt them and vanish.
            if (x<width/2+20 && x > width/2-140 && STATE!= DYING && STATE != DEAD){
                changeState(DEAD);
                view.hit();
                view.getPlayer().hurt();
            }

        }

    }
    //used to tell the Enemy that its state has changed, and to reposition itself based on what state it is in
    public void changeState(int newState){
        this.STATE=newState;
        if(newState==LEFTALIVE){
            this.x=0;
            this.y=height-300;

            paint.setAlpha(255);
        }
        else if(newState==RIGHTALIVE){
            this.x=width;
            this.y=height-300;

            paint.setAlpha(255);
        }
        else if(newState==UPPERRIGHT){
            this.x=width-300;
            this.y=0;
            paint.setAlpha(255);
        }
        else if(newState==UPPERLEFT){
            this.x=200;
            this.y=0;
            paint.setAlpha(255);
        }
    }
    public int getState(){
        return this.STATE;
    }
}
