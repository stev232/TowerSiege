package com.example.towerseige;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class GamePage extends AppCompatActivity
{
    private char direction = 'l';
    private boolean leftFinish, rightFinish, finish;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Set screen orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /* Code that I found that removes the title bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Sets no title, to the best of my knowledge
        getSupportActionBar().hide(); //Hides the title bar, to the best of my knowledge
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Sets the xml page to full screen removing the space that was being used for the title bar
        setContentView(R.layout.game_page); //Declare xml page for java file

        /* declare and initialize variables */
        final Button right = findViewById(R.id.rightHand);
        final Button left = findViewById(R.id.leftHand);
        final Button jump = findViewById(R.id.jump);
        final Button shoot = findViewById(R.id.shoot);
        final Button start = findViewById(R.id.start);
        ImageView character = (ImageView) findViewById(R.id.character);
        ImageView bullet = (ImageView) findViewById(R.id.bullet);
        ImageView enemyLeftBullet = (ImageView) findViewById(R.id.enemyLeftBullet);
        ImageView enemyRightBullet = (ImageView) findViewById(R.id.enemyRightBullet);
        ImageView enemyRight = (ImageView) findViewById(R.id.enemyRight);
        ImageView enemyLeft = (ImageView) findViewById(R.id.enemyLeft);

        ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams(); //get character image parameters
        ConstraintLayout.LayoutParams enemyCharacterParamsLeft = (ConstraintLayout.LayoutParams) enemyLeft.getLayoutParams();
        ConstraintLayout.LayoutParams enemyCharacterParamsRight = (ConstraintLayout.LayoutParams) enemyRight.getLayoutParams();

        enemyCharacterParamsLeft.leftMargin = characterParams.leftMargin - 1900;
        enemyCharacterParamsRight.rightMargin = characterParams.rightMargin - 2000;

        start.setOnClickListener(new View.OnClickListener()
        {
            Handler enemyLeftShoot = new Handler(); //initialize the shoot handler
            Handler enemyRightShoot = new Handler(); //initialize the shoot handler
            Handler hdnlrStart = new Handler(); //initialize the shoot handler

            @Override
            public void onClick(View v)
            {
                setCounter(0);
                gameStart(start, enemyLeftShoot, enemyRightShoot, hdnlrStart, enemyLeft, enemyRight, enemyLeftBullet, enemyRightBullet);
            }
        });

        /* character moves left */
        left.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                setDirection('l'); //set the direction that the character is facing
                character.setImageResource(R.drawable.character); //set the character to face left
                ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams(); //get character image parameters
                if(characterParams.leftMargin<1 && characterParams.rightMargin>1 && characterParams.leftMargin>-1400) //restrict how far to the left the character is allowed to move
                {
                    characterParams.leftMargin -= 40; //move character to the left
                }
                else if(characterParams.rightMargin<1)
                {
                    characterParams.rightMargin += 40;
                }
                else if(characterParams.leftMargin>-1400)
                {
                    characterParams.leftMargin -= 40; //move character to the left
                }
                character.setLayoutParams(characterParams); //set character parameters
                return false;
            }
        });

        /* character moves right */
        right.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                setDirection('r'); //set the direction that the character is facing
                character.setImageResource(R.drawable.characterright); //set the character to face right
                ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams(); //get character image parameters
                if(characterParams.rightMargin<1 && characterParams.leftMargin>1 && characterParams.rightMargin>-1600) //restrict how far to the right the character is allowed to move
                {
                    characterParams.rightMargin -= 40; //move the character to the right
                }
                else if(characterParams.leftMargin<1)
                {
                    characterParams.leftMargin += 40; //move character to the left
                }
                else if(characterParams.rightMargin>-1600)
                {
                    characterParams.rightMargin -= 40; //move the character to the right
                }
                character.setLayoutParams(characterParams); //set character parameters
                return true;
            }
        });

        /* initiate the shoot animation */
        shoot.setOnClickListener(new View.OnClickListener()
        {
            Handler handlrShoot = new Handler(); //initialize the shoot handler

            public void onClick(View v)
            {
                setFinish(false); //Parameter to determine when the event is over
                char direction = getDirection(); //determine which direction the character is facing

                playerShoot(character, bullet, handlrShoot, shoot, enemyLeft, enemyRight, direction);
            }
        });

        /* initiate the jump animation */
        jump.setOnClickListener(new View.OnClickListener()
        {
            Handler handlrJump = new Handler(); //initialize the shoot handler
            boolean finish; //declare boolean variable that will determine when the event is finished
            boolean up; //declare boolean variable that will determine when the apex of the jump is reached
            int counter;

            public void onClick(View v)
            {
                jump.setEnabled(false); //disable the jump button
                shoot.setEnabled(false); //disable the shoot button
                ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams(); //get character parameters
                up = true; //Parameter to determine if the character is jumping up or falling back down
                finish = false; //Parameter to determine when the event is over/* clear shoot events and set to null */
                counter = 0;
                Runnable characterJump = null;
                handlrJump.removeCallbacks(characterJump);
                handlrJump.removeCallbacksAndMessages(null);

                characterJump = new Runnable()  //begin shootRight event
                {
                    @Override
                    public void run()
                    {
                        ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams(); //get character parameters

                        if(characterParams.topMargin > -800 && up == true)
                        {
                            characterParams.topMargin-=25;
                            if(characterParams.topMargin<-790)
                            {
                                up = false;
                            }
                        }
                        else if(up == false && counter<35)
                        {
                            counter++;
                        }
                        else if(characterParams.topMargin < 1 && up == false && counter==35)
                        {
                            characterParams.topMargin+=25;
                            if(characterParams.topMargin>-1)
                            {
                                finish = true;
                                jump.setEnabled(true); //allow player to hit the jump button again
                                shoot.setEnabled(true); //allow player to hit the shoot button again
                            }
                        }

                        character.setLayoutParams(characterParams);
                        handlrJump.postDelayed(this, 10); //the bullet will move left every 50 milliseconds
                    }
                };
                if (finish)
                {
                    /* clear shoot event */
                    handlrJump.removeCallbacks(characterJump);
                    handlrJump.removeCallbacksAndMessages(characterJump);
                }
                else
                {
                    handlrJump.postDelayed(characterJump, 10); //the bullet will move left every 50 milliseconds
                }
            }
        });

        /* Set current images for play */
        setImage(); //set the hastily made floor image
    }// end onCreate

    public void gameStart(Button start, Handler enemyLeftShoot, Handler enemyRightShoot, Handler hndlrStart, ImageView enemyLeft, ImageView enemyRight, ImageView enemyLeftBullet, ImageView enemyRightBullet)
    {
        Runnable runStart = null;
        hndlrStart.removeCallbacks(runStart);
        hndlrStart.removeCallbacksAndMessages(null);
        start.setEnabled(false);
        setLeftFinish(true);
        setRightFinish(true);

        runStart = new Runnable()
        {
            @Override
            public void run()
            {
                if(getLeftFinish())
                {
                    setLeftFinish(false);
                    ConstraintLayout.LayoutParams bulletParamsLeft = (ConstraintLayout.LayoutParams) enemyLeftBullet.getLayoutParams(); //get bullet parameters
                    ConstraintLayout.LayoutParams enemyCharacterParamsLeft = (ConstraintLayout.LayoutParams) enemyLeft.getLayoutParams();

                    bulletParamsLeft.leftMargin = 0;

                    enemyLeft.setImageResource(R.drawable.enemy);
                    enemyLeft.setVisibility(View.VISIBLE);
                    enemyLeftBullet.setVisibility(View.VISIBLE);

                    /* clear shoot events and set to null */
                    Runnable enemyShootRight = null;
                    enemyLeftShoot.removeCallbacks(enemyShootRight);
                    enemyLeftShoot.removeCallbacksAndMessages(null);

                    bulletParamsLeft.leftMargin = (enemyCharacterParamsLeft.leftMargin + 120); //set the bullet horizontalBias based off of the character location
                    enemyLeftBullet.setVisibility(View.VISIBLE); //make bullet image visible
                    enemyLeftBullet.setLayoutParams(bulletParamsLeft); //set bullet parameters

                    enemyShootRight = new Runnable()  //begin shootRight event
                    {
                        @Override
                        public void run()
                        {
                            ConstraintLayout.LayoutParams bulletParamsLeft = (ConstraintLayout.LayoutParams) enemyLeftBullet.getLayoutParams(); //get bullet parameters

                            if (bulletParamsLeft.leftMargin < 1780 && getLeftFinish() == false)
                            {
                                bulletParamsLeft.leftMargin += 25;
                            }
                            if(bulletParamsLeft.leftMargin > 1730)
                            {
                                setLeftFinish(true);
                            }
                            enemyLeftBullet.setLayoutParams(bulletParamsLeft);

                            enemyLeftShoot.postDelayed(this, 40); //the bullet will move right every 50 milliseconds
                        }
                    };
                    if (getLeftFinish())
                    {
                        /* clear shoot event */
                        enemyLeftShoot.removeCallbacks(enemyShootRight);
                        enemyLeftShoot.removeCallbacksAndMessages(enemyShootRight);
                    }
                    else
                    {
                        enemyLeftShoot.postDelayed(enemyShootRight, 100); //the bullet will move right every 50 milliseconds
                    }
                }

                if(getCounter()>15)
                {
                    if(getRightFinish())
                    {
                        setRightFinish(false);
                        ConstraintLayout.LayoutParams bulletParamsRight = (ConstraintLayout.LayoutParams) enemyRightBullet.getLayoutParams(); //get bullet parameters
                        ConstraintLayout.LayoutParams enemyCharacterParamsRight = (ConstraintLayout.LayoutParams) enemyRight.getLayoutParams();

                        bulletParamsRight.rightMargin = 0;

                        enemyRight.setImageResource(R.drawable.enemy);
                        enemyRight.setVisibility(View.VISIBLE);
                        enemyRightBullet.setVisibility(View.VISIBLE);

                        /* clear shoot events and set to null */
                        Runnable enemyShootLeft = null;
                        enemyRightShoot.removeCallbacks(enemyShootLeft);
                        enemyRightShoot.removeCallbacksAndMessages(null);

                        bulletParamsRight.rightMargin =  (enemyCharacterParamsRight.rightMargin + 120); //set the bullet horizontalBias based off of the character location
                        enemyRightBullet.setVisibility(View.VISIBLE); //make bullet image visible
                        enemyRightBullet.setLayoutParams(bulletParamsRight); //set bullet parameters

                        enemyShootLeft = new Runnable()  //begin shootRight event
                        {
                            @Override
                            public void run()
                            {
                                ConstraintLayout.LayoutParams bulletParamsRight = (ConstraintLayout.LayoutParams) enemyRightBullet.getLayoutParams(); //get bullet parameters

                                if (bulletParamsRight.rightMargin < 1780 && getRightFinish() == false)
                                {
                                    bulletParamsRight.rightMargin += 25;
                                }
                                if(bulletParamsRight.rightMargin > 1730)
                                {
                                    setRightFinish(true);
                                }
                                enemyRightBullet.setLayoutParams(bulletParamsRight);

                                enemyRightShoot.postDelayed(this, 40); //the bullet will move left every 50 milliseconds
                            }

                        };
                        if (getRightFinish())
                        {
                            /* clear shoot event */
                            enemyRightShoot.removeCallbacks(enemyShootLeft);
                            enemyRightShoot.removeCallbacksAndMessages(enemyShootLeft);
                        }
                        else
                        {
                            enemyRightShoot.postDelayed(enemyShootLeft, 40); //the bullet will move left every 50 milliseconds
                        }
                    }
                }
                boostCounter();
                hndlrStart.postDelayed(this, 15); //the bullet will move left every 50 milliseconds
            }
        };
        if (finish)
        {
            /* clear shoot event */
            hndlrStart.removeCallbacks(runStart);
            hndlrStart.removeCallbacksAndMessages(runStart);
        }
        else
        {
            hndlrStart.postDelayed(runStart, 15); //the bullet will move left every 50 milliseconds
        }
    }

    public void playerShoot(ImageView character, ImageView bullet, Handler handlrShoot, Button shoot, ImageView enemyLeft, ImageView enemyRight, char direction)
    {
        /* grab parameters for both the bullet and character */
        ConstraintLayout.LayoutParams characterParams = (ConstraintLayout.LayoutParams) character.getLayoutParams();
        ConstraintLayout.LayoutParams bulletParams = (ConstraintLayout.LayoutParams) bullet.getLayoutParams();

        bulletParams.rightMargin = 0;
        bulletParams.leftMargin = 0;
        bullet.setLayoutParams(bulletParams);
        int holder;

        shoot.setEnabled(false); //Disable the shoot button

        /* clear shoot events and set to null */
        Runnable shootRight = null;
        handlrShoot.removeCallbacks(shootRight);
        handlrShoot.removeCallbacksAndMessages(null);
        Runnable shootLeft = null;
        handlrShoot.removeCallbacks(shootLeft);
        handlrShoot.removeCallbacksAndMessages(null);

        /* Begin selection structure to determine which direction to shoot the bullet */
        if (direction=='r') //shoot to the right
        {
            if(characterParams.rightMargin < 1 && characterParams.leftMargin > -1)
            {
                bulletParams.rightMargin = (characterParams.rightMargin - 120); //set the bullet horizontalBias based off of the character location
            }
            else if(characterParams.leftMargin < -1)
            {
                if (characterParams.leftMargin < -120)
                {
                    bulletParams.leftMargin = (characterParams.leftMargin + 120); //set the bullet horizontalBias based off of the character location
                }
                else
                {
                    bulletParams.rightMargin = -(characterParams.leftMargin + 120);
                }
            }
            bullet.setVisibility(View.VISIBLE); //make bullet image visible
            bullet.setLayoutParams(bulletParams); //set bullet parameters

            shootRight = new Runnable()  //begin shootRight event
            {
                @Override
                public void run()
                {
                    int holder;
                    ConstraintLayout.LayoutParams bulletParams = (ConstraintLayout.LayoutParams) bullet.getLayoutParams(); //get bullet parameters

                    if (direction == 'r')
                    {
                        bulletParams = (ConstraintLayout.LayoutParams) bullet.getLayoutParams(); //get bullet parameters

                        if (bulletParams.rightMargin > -1640 && bulletParams.leftMargin>-1 && bulletParams.rightMargin<1)
                        {
                            bulletParams.rightMargin -= 45;
                        }
                        else if(bulletParams.leftMargin<1)
                        {
                            if (bulletParams.leftMargin > -44 && bulletParams.rightMargin < 1)
                            {
                                holder = bulletParams.leftMargin;
                                bulletParams.leftMargin = 0;
                                bulletParams.rightMargin = -(45 + holder);
                            }
                            else
                            {
                                bulletParams.leftMargin += 45;
                            }
                        }
                        else if(bulletParams.rightMargin > -1640 && bulletParams.rightMargin<1)
                        {
                            bulletParams.leftMargin -= 35;
                        }

                        if (bulletParams.rightMargin < -1580)
                        {
                            if(enemyRight.getVisibility() == View.VISIBLE)
                            {
                                enemyRight.setVisibility(View.INVISIBLE);
                            }
                            bullet.setVisibility(View.INVISIBLE); //set the image holder to invisible
                            setFinish(true); //The event has concluded
                            shoot.setEnabled(true); //allow player to hit the shoot button again
                        }

                        bullet.setLayoutParams(bulletParams);
                    }
                    handlrShoot.postDelayed(this, 40); //the bullet will move right every 50 milliseconds
                }

            };
            if (getFinish())
            {
                /* clear shoot event */
                handlrShoot.removeCallbacks(shootRight);
                handlrShoot.removeCallbacksAndMessages(shootRight);
            }
            else
            {
                handlrShoot.postDelayed(shootRight, 40); //the bullet will move right every 50 milliseconds
            }
        }
        else if (direction=='l') //shoot to the right
        {
            if(characterParams.leftMargin < 1 && characterParams.rightMargin > -1)
            {
                bulletParams.leftMargin = (characterParams.leftMargin - 120); //set the bullet horizontalBias based off of the character location
            }
            else if(characterParams.rightMargin < -1)
            {
                if (characterParams.rightMargin < -120)
                {
                    bulletParams.rightMargin = (characterParams.rightMargin + 120); //set the bullet horizontalBias based off of the character location
                }
                else
                {
                    bulletParams.leftMargin = -(characterParams.rightMargin + 120);
                }
            }
            bullet.setVisibility(View.VISIBLE); //make bullet image visible
            bullet.setLayoutParams(bulletParams); //set bullet parameters

            shootLeft = new Runnable() //begin shootLeft event
            {
                @Override
                public void run()
                {
                    ConstraintLayout.LayoutParams bulletParams = (ConstraintLayout.LayoutParams) bullet.getLayoutParams(); //get bullet parameters
                    int holder;

                    if (direction == 'l')
                    {
                        bulletParams = (ConstraintLayout.LayoutParams) bullet.getLayoutParams(); //get bullet parameters

                        if (bulletParams.leftMargin > -1400 && bulletParams.rightMargin>-1 && bulletParams.leftMargin<1)
                        {
                            bulletParams.leftMargin -= 45;
                        }
                        else if(bulletParams.rightMargin<1)
                        {
                            if (bulletParams.rightMargin > -44 && bulletParams.leftMargin < 1)
                            {
                                holder = bulletParams.rightMargin;
                                bulletParams.rightMargin = 0;
                                bulletParams.leftMargin = -(45 + holder);
                            }
                            else
                            {
                                bulletParams.rightMargin += 45;
                            }
                        }
                        else if(bulletParams.leftMargin > -1400 && bulletParams.leftMargin<1)
                        {
                            bulletParams.leftMargin -= 35;
                        }

                        if (bulletParams.leftMargin < -1380)
                        {
                            if(enemyLeft.getVisibility() == View.VISIBLE)
                            {
                                enemyLeft.setVisibility(View.INVISIBLE);
                            }
                            bullet.setVisibility(View.INVISIBLE); //set the image holder to invisible
                            setFinish(true); //The event has concluded
                            shoot.setEnabled(true); //allow player to hit the shoot button again
                        }
                        bullet.setLayoutParams(bulletParams);
                    }
                    handlrShoot.postDelayed(this, 40); //the bullet will move left every 50 milliseconds
                }
            };
            if (finish)
            {
                /* clear shoot event */
                handlrShoot.removeCallbacks(shootLeft);
                handlrShoot.removeCallbacksAndMessages(shootLeft);
            }
            else
            {
                handlrShoot.postDelayed(shootLeft, 40); //the bullet will move left every 50 milliseconds
            }
        }

    }

    private int getCounter()
    {
        return this.counter;
    }

    private void boostCounter()
    {
        this.counter++;
    }

    private void setCounter(int value)
    {
        this.counter = value;
    }

    private void setLeftFinish(boolean finish)
    {
        this.leftFinish = finish;
    }

    private boolean getLeftFinish()
    {
        return this.leftFinish;
    }

    private void setRightFinish(boolean finish)
    {
        this.rightFinish = finish;
    }

    private boolean getRightFinish()
    {
        return this.rightFinish;
    }

    private void setFinish(boolean finish)
    {
        this.finish = finish;
    }

    private boolean getFinish()
    {
        return this.finish;
    }

    private void setDirection(char direction)
    {
        this.direction = direction;
    }

    private char getDirection()
    {
        return this.direction;
    }

    //Set the floor image in the xml file
    public void setImage()
    {
        ImageView source = (ImageView) findViewById(R.id.floor);
        source.setImageResource(R.drawable.floorconcept);
    }

    /* This method will bring the player back to the landing screen */
    public void landingScreen(View view)
    {
        Intent intent = new Intent(this, LandingScreen.class);
        startActivity(intent);
    }
}