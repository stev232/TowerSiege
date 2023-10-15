package com.example.towerseige;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

//The LandingScreen is the homepage of the app and is the main page
public class LandingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Set screen orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /* Code that I found that removes the title bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Sets no title, to the best of my knowledge
        getSupportActionBar().hide(); //Hides the title bar, to the best of my knowledge
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Sets the xml page to full screen removing the space that was being used for the title bar
        setContentView(R.layout.landing_screen); //Declare xml page for java file

        /* Set current images for play */
        setImage(); //set the hastily made floor image
    }

    /*public void PlayBackGroundMusic()
    {
        Intent intent = new Intent(LandingScreen.this, BackGroundMusic.class);
        startService(intent);
    }*/

    //Set the floor image in the xml file
    public void setImage()
    {
        ImageView background = (ImageView) findViewById(R.id.floor);
        background.setImageResource(R.drawable.floorconcept);
    }

    //This method brings you to the game page
    public void startGame(View view)
    {
        Intent intent = new Intent(this, GamePage.class);
        startActivity(intent);
    }

    //This method brings you to the help screen
    public void helpScreen(View view)
    {
        Intent intent = new Intent(this, HelpScreen.class);
        startActivity(intent);
    }

    //This method brings you to the settings screen
    public void settingsPage(View view)
    {
        Intent intent = new Intent(this, SettingsPage.class);
        startActivity(intent);
    }
}