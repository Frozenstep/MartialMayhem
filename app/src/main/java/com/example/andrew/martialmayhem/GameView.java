package com.example.andrew.martialmayhem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.*;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private int gameTimer = 0;
    private int width;
    private int height;
    private int x, y;
    private boolean what;
    private Paint red;
    private final int NUETRAL = 0;
    private final int TOPRIGHT = 1;
    private final int BOTTOMRIGHT = 2;
    private final int BOTTOMLEFT = 3;
    private final int TOPLEFT = 4;
    private final int HURT = 5;
    private final int GAMETIMERCONST = 30;
    private int lastTouch = 0;
    private Player player;
    private Enemy[] enemy;
    private int spawnCounter = 0;
    private Random rand;
    private int enemyCounter = 0;
    private LinkedList<Enemy> enemyList= new LinkedList<>();

    //set between any negative number to 119 to decrease -> increase spawn rate. Lower==easier
    private int difficulty = 60;

    public GameView(Context context) {
        super(context);
        makeDrawingTools();
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    //helping myself stay organized, this basically creates all the tools/assets I'll be using to draw stuff
    public void makeDrawingTools() {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        red = new Paint();
        red.setColor(Color.rgb(250, 0, 0));
        player = new Player(this.getContext(), width, height);
        //Context context = this.getContext();
        enemy = new Enemy[10];
        rand = new Random();

        // int  n = rand.nextInt(400) -200;
        /*for (int i = 0; i < 10; i++) {
            enemy[i] = new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.ninjastar), i, width, height, this);
            //n = rand.nextInt(400) -200;

        }*/

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    public void update() {
        //quick half-second timer to reset player back to neutral position after striking/getting struck
        if (gameTimer > 0) {
            --gameTimer;
            if (gameTimer == 0) {
                lastTouch = NUETRAL;
            }
        }
        if(spawnCounter<0){
            spawnCounter=30;
            Enemy temp = new Shuriken(BitmapFactory.decodeResource(getResources(), R.drawable.ninjastar), 0, width, height, this);
            enemyList.addLast(temp);
            enemyList.get(enemyList.size()-1).changeState((rand.nextInt(4) + 1));
        }
        --spawnCounter;
        //controls enemy spawn rate, somewhat randomizes when enemies spawn
       /* if (spawnCounter <= 0) {
            spawnCounter = rand.nextInt(120 - difficulty) + 60;
            if (enemy[enemyCounter].getState() == 0) {
                enemy[enemyCounter].changeState((rand.nextInt(4) + 1));
                ++enemyCounter;
                if (enemyCounter == 10) {
                    enemyCounter = 0;
                }
            }
        }
        --spawnCounter;
        */
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            if (lastTouch == TOPRIGHT) {
                //canvas.drawRect(width/2,0, width, height/2, red);
                player.setPosition(5);
            } else if (lastTouch == BOTTOMRIGHT) {
                //canvas.drawRect(width/2, height/2, width, height, red);
                player.setPosition(1);
            } else if (lastTouch == BOTTOMLEFT) {
                //canvas.drawRect(0, height/2, width/2, height, red);
                player.setPosition(2);
            } else if (lastTouch == TOPLEFT) {
                //canvas.drawRect(0,0, width/2, height/2, red);
                player.setPosition(4);
            } else if (lastTouch == HURT) {
                player.setPosition(3);
            } else {
                player.setPosition(0);
            }
            player.drawSelf(canvas);
            if (gameTimer == GAMETIMERCONST-2){
                for(int i=0; i<enemyList.size(); i++){
                    enemyList.get(i).drawSelf(canvas, lastTouch);
                    if(enemyList.get(i).getState()==0){
                        enemyList.remove(i);
                        --i;
                    }
                }
            }
            else {
                for (int i = 0; i < enemyList.size(); i++) {
                    enemyList.get(i).drawSelf(canvas, 0);
                    if (enemyList.get(i).getState() == 0) {
                        enemyList.remove(i);
                        --i;
                    }
                }
            }
            /*
            if (gameTimer == 29) {
                for (int i = 0; i < 10; i++) {
                    enemy[i].drawSelf(canvas, lastTouch);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    enemy[i].drawSelf(canvas, 0);
                }
            }
            */
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                //split the screen into 4 quarters, pressing each quarter of the screen will activate an attack in that direction
                if (x > width / 2) {
                    if (y > height / 2) {
                        lastTouch = BOTTOMRIGHT;
                    } else {
                        lastTouch = TOPRIGHT;
                    }
                } else {
                    if (y > height / 2) {
                        lastTouch = BOTTOMLEFT;
                    } else {
                        lastTouch = TOPLEFT;
                    }
                }
                gameTimer = GAMETIMERCONST;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
        //return super.onTouchEvent(event);
    }

    //sets the player's sprite to the "being hit" one
    public void hit() {
        this.lastTouch = HURT;
        gameTimer = GAMETIMERCONST;
    }

    public Player getPlayer() {
        return player;
    }
}

