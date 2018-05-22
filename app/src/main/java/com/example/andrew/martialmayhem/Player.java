package com.example.andrew.martialmayhem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public class Player {
    //TODO: Make the art nicer, add sprites so we can randomize what kind of attacks the player can do, add sounds, an HP bar, a lose condition...
    private Bitmap player;
    private Bitmap punch;
    private Bitmap leftPunch;
    private Bitmap hurt;
    private Bitmap kick;
    private Bitmap leftKick;
    private int position=0;

    //variable used to re-center the position of the player
    private final int OFFSET=200;
    //const variables used to signal what the player should be doing this frame
    private final int NUETRAL=0;
    private final int RIGHTPUNCH=1;
    private final int LEFTPUNCH=2;
    private final int HURT=3;
    private final int UPPERLEFT=4;
    private final int UPPERRIGHT=5;

    private Matrix matrix;

    //position of player.
    private int x, y;
    Player(Context context, int width, int height) {

        this.player = BitmapFactory.decodeResource(context.getResources(),R.drawable.playernuetral);
        this.punch = BitmapFactory.decodeResource(context.getResources(),R.drawable.playerpunch);
        matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        leftPunch = Bitmap.createBitmap(punch, 0, 0, punch.getWidth(), punch.getHeight(), matrix, false);
        this.hurt=BitmapFactory.decodeResource(context.getResources(),R.drawable.playerhit);
        this.kick = BitmapFactory.decodeResource(context.getResources(),R.drawable.playerkick);
        this.leftKick = Bitmap.createBitmap(kick, 0, 0, kick.getWidth(), kick.getHeight(), matrix, false);
        x = width/2;
        y = height/2;
    }
    //Tells the player to draw itself on the canvas
    public void drawSelf(Canvas canvas){
        if(position==NUETRAL) {
            canvas.drawBitmap(player, x-OFFSET, y, null);
        }
        else if(position==RIGHTPUNCH){
            canvas.drawBitmap(punch, x+100-OFFSET, y, null);
        }
        else if(position==LEFTPUNCH){
            canvas.drawBitmap(leftPunch, x-100-OFFSET, y, null);
        }
        else if(position==UPPERLEFT){
            canvas.drawBitmap(leftKick, x-100-OFFSET, y, null);
        }
        else if(position==UPPERRIGHT){
            canvas.drawBitmap(kick, x-OFFSET, y, null);
        }
        else if(position==HURT){
            canvas.drawBitmap(hurt, x-200, y, null);
        }
    }
    public int getPosition(){
        return position;
    }

    public void setPosition(int set){
        this.position=set;
    }
    public void hurt(){
        position=HURT;
    }
}
