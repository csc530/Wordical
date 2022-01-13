package com.example.wordical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void SinglePlayer(View view)
    {//sets the number of players to one
        Bundle bundle = new Bundle();
        bundle.putInt("Players", 1);//adds to the bundle the number of players, 1
        Game(bundle);//sends the bundle to the next screen
    }

    public void Multiplayer(View view)
    {
        NumberPicker num = (NumberPicker) findViewById(R.id.Players);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);
        num.setMaxValue(5);//sets the max value of the numberpicker as 5
        num.setMinValue(2);//sets the minimium number for the number picker as 2
    }

    public void Confirm(View view)
    {
        NumberPicker num = (NumberPicker) findViewById(R.id.Players);
        int n = num.getValue();
        Bundle bundle = new Bundle();
        bundle.putInt("Players", n);//adds to the bundle the number of selected players
        Game(bundle);//sends the bundle to the next screen
    }

    public void Game(Bundle bundle)
    {//goes to the player name selection screen sending the along the bundle and any other settings
        Intent intent = new Intent(StartScreen.this, Naming.class);
        intent.putExtras(bundle);
        if(getIntent().getExtras() != null)
            intent.putExtras(getIntent().getExtras());
        else
        {
            bundle.putChar("GameLength", 'L');
            bundle.putBoolean("Dictionary", false);
            bundle.putBoolean("Hints", false);
        }
        startActivity(intent);
    }

    public void settings(View view)
    {//goes to the settings and rules screen
        Intent intent = new Intent(StartScreen.this, Settings.class);
        startActivity(intent);
    }
}