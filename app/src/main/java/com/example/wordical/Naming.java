package com.example.wordical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Naming extends AppCompatActivity {

    int players;
    String playernames[];
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);
        Bundle bundle = getIntent().getExtras();
        players = bundle.getInt("Players");
        playernames = new String[players];//creates a string array to hold the names for each player
        if(players == 1)
        {
            Button button = (Button) findViewById(R.id.next);
            button.setText("Play");
        }
    }

    public void generic(View view)
    {//creates ageneric name for each player and starts the game
        Intent intent = new Intent(Naming.this, MainActivity.class);
        Bundle bun = getIntent().getExtras();
        for(int i = 0; i < playernames.length; i++)
            playernames[i] = "Player " + (i+1) + "";
        bun.putStringArray("PlayersNames", playernames);
        intent.putExtras(bun);
        startActivity(intent);
    }

    public void next(View view)
    {//collects the name of the player and allows to enter the name of the next
        Button button = (Button) findViewById(R.id.next);
        EditText name = (EditText) findViewById(R.id.name);
        String txt = name.getText().toString();
        if(!txt.equals(""))
            playernames[count] = name.getText().toString();
        else
            playernames[count] = "Player " + (count+1);
        count++;
        TextView Title = (TextView) findViewById(R.id.title);
        Title.setText("Player " + (count+1) + "'s name");
        if(count >= players-1)
            button.setText("Play");
        else
            button.setText("Next");
        name.setText("");
        button = (Button) findViewById(R.id.back);
        if(count <= 0)
            button.setText("Exit");
        else
            button.setText("Previous");
        if(count >= players)//if there no more players to name starts game
        {
            Intent intent = new Intent(Naming.this, MainActivity.class);
            Bundle bundle = getIntent().getExtras();
            bundle.putStringArray("PlayersNames", playernames);//sends the array with all the players name
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void previous(View view)
    {//goes back to the previous player name to change it
        TextView Title = (TextView) findViewById(R.id.title);
        count--;
        Title.setText("Player " + (count+1) + "'s name");
        Button button =(Button) findViewById(R.id.back);
        EditText name = (EditText) findViewById(R.id.name);
        name.setText("");
        name.setHint(playernames[count]);
        if(count <= 0)
            button.setText("Exit");
        else
            button.setText("Previous");
        if(button.getText().toString().equals("Exit"))//if there are no more previous players return to the home screen
        {
            Intent intent = new Intent(Naming.this, StartScreen.class);
            startActivity(intent);
        }
    }
}