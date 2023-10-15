package com.example.towerseige;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Set screen orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /* Code that I found that removes the title bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE); //Sets no title, to the best of my knowledge
        getSupportActionBar().hide(); //Hides the title bar, to the best of my knowledge
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Sets the xml page to full screen removing the space that was being used for the title bar
        setContentView(R.layout.settings_page);


        Intent intent = new Intent(SettingsPage.this, BackGroundMusic.class);
        startService(intent);

        /* Code that takes the SeekBar and uses it as a volume slider */
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volControl = (SeekBar)findViewById(R.id.volumeSlider);
        volControl.setMax(maxVolume); //This line determines the value that volControl will stop at
        volControl.setProgress(curVolume); //This line displays where the current volume value is stored on the seekBar
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
            {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0); //This line takes the relative position of the seekbar and translates it too a volume value
            }
        });

    }

    protected void onDestroy()
    {
        super.onDestroy();
        Intent intent = new Intent(SettingsPage.this, BackGroundMusic.class);
        stopService(intent);
    }


    protected void onPause()
    {
        super.onPause();
        Intent intent = new Intent(SettingsPage.this, BackGroundMusic.class);
        stopService(intent);
    }

    protected void onResume()
    {
        super.onResume();
    }

    //This method brings you from the Help Screen back to the main menu
    public void landingScreen(View view)
    {
        Intent intent = new Intent(this, LandingScreen.class);
        startActivity(intent);
    }

    public void musicPlayer(View view)
    {
        Intent intent = new Intent(SettingsPage.this, BackGroundMusic.class);
        startService(intent);
    }
}