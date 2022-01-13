package com.example.wordical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Win extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        int plyNum = bundle.getBundle("Settings").getInt("Players");
        if(plyNum > 1)//if there was more than one player in the game
        {//get the data of all players from the game
            int scores[] = bundle.getIntArray("playerScores");
            String playerNames[] = bundle.getStringArray("playerNames");
            int highscores[] = bundle.getIntArray("playerHighScores");
            String BestWords[] = bundle.getStringArray("playerBestWords");
            String string[] = new String[plyNum];//a string array to hold the text of information of the game played
            for(int i = 0; i < string.length; i++)
                string[i] = playerNames[i] + "'s final score: " + scores[i] + "WP." + "Their Best Word \"" + BestWords[i] + "\"played for " + highscores[i] + "WP";
            TextView text = (TextView) findViewById(R.id.Winner);
            text.setText(string[0]);//sets the winner's info in the large textbox
            text = (TextView) findViewById(R.id.Textbox1);
            for (int i = 1; i < string.length; i++)
                text.append(string[i] +"      ");//sets everyone else's information in descending order in the textbox below
        }
        else//if there was only one player in the game
        {//gets the data of the one player thorughout the game
            String words[] = intent.getStringArrayExtra("WordRankingsByUSE");
            int scores[] = intent.getIntArrayExtra("ScoreRankingsByUSE");
            History log = new History(scores, words);
            scores = bundle.getIntArray("playerScores");
            String playerNames[] = bundle.getStringArray("playerNames");
            log.sortByscore();//sorts the players played words by score
            TextView text = (TextView) findViewById(R.id.Winner);
            String txt ="Your final score "+ playerNames[0] + " was: " + scores[0] + "WP";//indicates their final score in the game
            text.setText(txt);
            text = (TextView) findViewById(R.id.Textbox1);
            text.setText("");
            log.sortByscore();
            for(int i = 0; i < log.size(); i++)
                text.append(log.print(i) + "     \n") ;//shows their played words in descending order by score
        }
    }

    public void Replay(View view)
    {
        Bundle bundle = getIntent().getBundleExtra("Settings");
        Intent intent = new Intent(Win.this, StartScreen.class);
        intent.putExtras(bundle);//sends all this game's settings
        startActivity(intent);//goes back to startscreen
    }
}