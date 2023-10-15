package com.example.towerseige;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class HelpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Set screen orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /* Code that I found that removes the title bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Sets no title, to the best of my knowledge
        getSupportActionBar().hide(); //Hides the title bar, to the best of my knowledge
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Sets the xml page to full screen removing the space that was being used for the title bar
        setContentView(R.layout.help_screen); //Declare xml page for java file

        /* Set current images for play */
        setImage(); //set the hastily made floor image
    }

    //Set the floor image in the xml file
    public void setImage()
    {
        ImageView source = (ImageView) findViewById(R.id.floor);
        source.setImageResource(R.drawable.floorconcept);
    }

    //This method brings you from the Help Screen back to the main menu
    public void landingScreen(View view)
    {
        Intent intent = new Intent(this, LandingScreen.class);
        startActivity(intent);
    }
}