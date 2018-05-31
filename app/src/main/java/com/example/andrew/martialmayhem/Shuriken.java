package com.example.andrew.martialmayhem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

public class Shuriken extends Enemy {
    private int rotation=0;
    private Bitmap star;
    private Bitmap rotatedStar;
    //size of the screen
    private GameView view;
    Shuriken(Bitmap input, int width, int height, GameView View){
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
    public void drawSelf(Canvas canvas, int playerAction){

        if(STATE!=INACTIVE) {
            //first a quick check to see the player attacked, and if so, check if this enemy is within range. If we are, we go into the dying state.
            if(playerAction==PLAYERBOTTOMRIGHT && x<width/2+220 && x > width/2+20 && STATE==RIGHT){
                changeState(LEFTDYING);
            }
            else if(playerAction==PLAYERBOTTOMLEFT && x<width/2-140 && x > width/2-340 && STATE==LEFT){
                changeState(LEFTDYING);
            }
            else if(playerAction==PLAYERTOPLEFT && x<width/2-140 && x > width/2-340 && STATE==UPPERLEFT){
                changeState(LEFTDYING);
            }
            else if(playerAction==PLAYERTOPRIGHT && x<width/2+220 && x > width/2+20 && STATE==UPPERRIGHT){
                changeState(LEFTDYING);
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
            if (STATE==LEFT){
                x+=(speed+1);
            }
            else if (STATE==RIGHT){
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
            else if(STATE==LEFTDYING){
                y-=2;
                x+=2;
                paint.setAlpha(paint.getAlpha()-10);
                if(paint.getAlpha()<10){
                    changeState(INACTIVE);
                }
            }
            //if enemy isn't dying and manges to touch the player, hurt them and vanish.
            if (x<width/2+20 && x > width/2-140 && STATE!= LEFTDYING && STATE != INACTIVE){
                changeState(INACTIVE);
                view.hit();
                view.getPlayer().hurt();
            }

        }

    }
    public void spawn(){
        Random rand = new Random();
        STATE = (rand.nextInt(4) + 1);
        if(STATE==LEFT){
            this.x=0;
            this.y=height-300;

        }
        else if(STATE==RIGHT){
            this.x=width;
            this.y=height-300;

        }
        else if(STATE==UPPERRIGHT){
            this.x=width-300;
            this.y=0;
        }
        else if(STATE==UPPERLEFT){
            this.x=200;
            this.y=0;
        }
    }
    public int getState(){
        return this.STATE;
    }
    public String getType(){
        return "Shuriken";
    }
}
