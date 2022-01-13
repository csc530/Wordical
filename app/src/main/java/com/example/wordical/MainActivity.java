/*
Christofer Cousins
Wordical Final Game
June 15 2020
 */
package com.example.wordical;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.Gravity.CENTER;


public class MainActivity extends AppCompatActivity {

    Deck deck;//sets up Deck
    Card hand[] = new Card[5];//sets up cards in players hand
    Player players[];//sets up amount of players in a game
    int turn = 0;//sets up up turn counter for game
    Dice Dice1 = new Dice();//sets up up first vowel dice
    Dice Dice2 = new Dice();//sets up up second vowel dice
    int tries = 0;//sets up amount of tries before player loses their turn
    Dictionary dictionary;//sets up up in-game dictinary for word-checking and providing hints
    History log = new History();//sets up the history of all words used in the game
    String Hint = "";//sets up hint string to help the player
    int hintCount = 0;//setups up how beneficial the hints are after time
    boolean wordCheck;//varaiable indicating to check if their word is in the in-game dictionary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();//gets all the settings for this game
        players = new Player[bundle.getInt("Players")];//sets the size of player array indicating how many players there are in this game
        String names[] = bundle.getStringArray("PlayersNames");//gets each players name
        for(int i = 0; i < players.length; i++)//assigns each player their name
            players[i] = new Player(names[i] + "");
        char gameLength = bundle.getChar("GameLength");//gets the size of the deck they choose to play with
        wordCheck = bundle.getBoolean("Dictionary");//update the variable if they'd like to play with the in-game dictionary
        boolean hints  = bundle.getBoolean("Hints");//variable indicating if they'd like to play with hints on
        deck = new Deck(gameLength);//setups up the deck in the appropriate size
        setup();//sets up the play screen
        if(wordCheck || hints)//initializes the dictionary object if they chose to play with the in-game dictionary or hints
            dictionary = new Dictionary(this);
        if(!hints)//if they are not playing with hints removes hint button from the screen
        {
            Button button = (Button) findViewById(R.id.hintButton);
            button.setVisibility(View.GONE);
        }
        if(players.length == 1)//if there is one player
        {
            TextView t = (TextView) findViewById(R.id.playerscore);
            t.setVisibility(View.VISIBLE);//makes a seperate text line of text visible to display their score
        }
    }

    public void draw()//sets each picture for the cards and dice on screen
    {
        ImageView image = (ImageView) findViewById(R.id.a);
        hand[0].setImage(image);
        image = (ImageView) findViewById(R.id.b);
        hand[1].setImage(image);
        image = (ImageView) findViewById(R.id.c);
        hand[2].setImage(image);
        image = (ImageView) findViewById(R.id.d);
        hand[3].setImage(image);
        image = (ImageView) findViewById(R.id.e);
        hand[4].setImage(image);
        Dice2.setImage((ImageView) findViewById(R.id.D2));
        Dice1.setImage((ImageView) findViewById(R.id.D1));
    }

    public int CalculatePoints(String Wordical)//calculates the points of their submitted word
    {
        int total = 0;//sets up total points of the word
        String consonant = "";//sets up the letters given in hand
        for(int i = 0; i < hand.length; i++)//puts each letter in the player's hand to the Hand variable
            consonant+=hand[i].getLetter()+"";
        for(int i = 0; i < Wordical.length(); i++)//cycles through their submitted word letter by letter
        {
            int pos = consonant.indexOf(Wordical.charAt(i));//indicates if the letter in player's word is found in their consonant cards
            if(pos != -1)//if it found in their consonant cards
                total+=hand[pos].getPoints();//adds the points from that card to the total value
            else
            {
                char letter = Wordical.charAt(i);
                if(letter == Dice1.getLetter() || letter == Dice2.getLetter())//if the letter is found on one of their dice
                    total++;//adds one point
            }//if it's not found on their dice or in their cards no points are added because it came from the wild side of the vowel dice
        }
        if(Wordical.equals(""))//if the could not come up with a word or no word
            total++;//adds one point
        return total;
    }

    public void Submit(View view)
    {
        TextView input = (TextView) findViewById(R.id.Wordical);
        String Word = input.getText().toString();//gets their entered word
        int points = CalculatePoints(Word);//gets the score of their entered word
        boolean english;
        if(wordCheck)//if they chose to use the in-game dictionary to check their words
            english = dictionary.Find(Word);//check to see if their word is in the dictionary
        else
            english = true;
        boolean Reused = log.isUsed(Word);//check to see if someone has already used that word
        boolean min2 = Word.length() >= 2;//check to make sure their word is at least two letters long
        if(english && !Reused && min2)//check if it's and english word in the in-game dictionary, that the word has not been reused, and at least 2 letters long
            confirmDB(Word, points);
        else
        {
            if(tries < 3)//if they tried less than 3 times displays what they have done wrong
            {
                if(!english)
                    Toast.makeText(getApplicationContext(), "Not an english word, sorry.\n(๑′°︿°๑)", Toast.LENGTH_SHORT).show();
                else if(Reused)
                    Toast.makeText(getApplicationContext(), "Sorry, someone already used that word.\n(๑′°︿°๑)", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Sorry, your word must be at least be 3 letters long.\n(๑′°︿°๑)", Toast.LENGTH_SHORT).show();
                tries++;
            }
            else//skips the players turn if they used up their three tries
            {
                input.setText(""); //makes their word nothing as they used up all their tries, this will add them the score of one
                skippedDB();
                tries = 0;//resets tries for next turn
                play();
            }
        }
    }

    public void confirmDB(String Word, int points) {//creates dialog box if the player wishes to confirm their word
        AlertDialog.Builder confirm = new AlertDialog.Builder(MainActivity.this);//intializes a pop-up box in this screen
        DialogInterface.OnClickListener Dclick = new DialogInterface.OnClickListener() {//creates an onclick listener that closes the dialog box
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
            //creates an onclick listener that closes the dialog box and then plays the player's word
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                play();
            }
        };
        confirm.setMessage("Are you sure you want to play \"" + Word + "\" for " + points + "WP?");//sets the message in the dialog box
        confirm.setNegativeButton("Decline", Dclick);//creates the no button for the dialog box
        confirm.setPositiveButton("Confirm", onClick);//creates yes button for the dialog box
        confirm.setTitle("Are you sure?");//creates the title for dialog box
        confirm.create();//creates dialog box
        confirm.show();//displays the dialog box to the user
    }

    public void skippedDB() {//dialog box for when a player misses their turn
        AlertDialog.Builder skipped = new AlertDialog.Builder(MainActivity.this);//intializes the dialog box in this screen
        skipped.setMessage("Sorry looks like you couldn't come up with a word in 3 tries. Your turn has been skipped");//sets message in the dialog box
        DialogInterface.OnClickListener OK = new DialogInterface.OnClickListener() {//creates an onclick listener
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();//closes the dialog box
            }
        };
        skipped.setPositiveButton("Ok", OK);//creates yes button
        skipped.setTitle("Uh Oh! [O.O]");//creates title for dialog box
        skipped.create();//creates dialog box
        skipped.show();//displays dialog box
    }

    public void NextPlayer()
    {
        AlertDialog.Builder next = new AlertDialog.Builder(MainActivity.this);
        int num;
        if(players.length == 1)
            num = 0;
        else
            num = turn;
        String text = players[num].getName() + " it's now your turn. Get ready!";
        next.setMessage(text);
        DialogInterface.OnClickListener OK = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        next.setPositiveButton("OK!", OK);
        if(players.length != 1)//if there is more than one player
            next.setTitle(players[turn].getName());//sets dialog box title to the now player's name
        next.create();
        next.show();
    }

    public void play()
    {
        TextView txt = (TextView) findViewById(R.id.Wordical);
        String Word = txt.getText().toString();//gets submitted word from player
        int total = CalculatePoints(Word);//caluclates the score of the word
        if(players.length == 1)//if there is one player
        {
            players[0].addToScore(total);//adds score to their total
            players[0].isBestWord(Word, total);//saves the score and word if it's their best one yet
            turn++;//increments the turn
        }
        else
        {
            players[turn].addToScore(total);//adds score to their total
            players[turn].isBestWord(Word, total);//saves the score and word if it's their best one yet
            if (turn == players.length - 1)//if it was the last player's turn
                turn = 0;//sets it to the first player's turn
            else
                turn++;//next player's turn
        }
        log.add(Word, total);//adds words to the history to prevent repeats
        setup();//updates screen
    }

    public void setup()
    {
        TextView text = (TextView)findViewById(R.id.playerName);
        String txt;
        if(players.length == 1)//if there is one player
        {
            txt = "Turn: " + turn;
            text.setText(txt);//displays new turn
            txt = "Score: " + players[0].getScore() + "WP";
            text = (TextView) findViewById(R.id.playerscore);
            text.setText(txt);//updates the players score
        }
        else
        {
            txt = players[turn].getName() + ":   " + players[turn].getScore() + "WP";
            text.setText(txt);//updates the player's name with their score
        }
        if(deck.size() >= 5)//if there are more than 5 cards left in the deck
        {
            for (int i = 0; i < hand.length; i++)
                hand[i] = deck.pop();//gives the player the next set of 5 consonant cards
            NextPlayer();//displays the pop-up box that is is now the next player's turn
        }
        else if(deck.size() > 0)//if there are less than 5 cards left in the deck
        {
            int D = deck.size();
            for(int i = 0; i < D; i++)
                hand[i] = deck.pop();//gives the player the next set of remaining consonant cards
            for(int i = D; i < hand.length; i++)
                hand[i].setLetter('/');//makes the rest of the 5 consannt cards unplayable
        }
        else//if there no more consonant cards left in the deck
            win();//proceeds to win screen
        text = (TextView) findViewById(R.id.Wordical);
        text.setText("");//sets their inputed text to nohting
        text.setHint("Perhaps a verb?");//gives them a hint of a type of word that could be formed form their cards and dice
        Roll();//rolls the dice
        draw();//updates the images of cards and dice
        hintCount = 0;//resets the level of help form the hints
        tries = 0;//reset their tries to zero
    }

    public void win()
    {
        Intent intent = new Intent(MainActivity.this, Win.class);//makes an intent to go to the win screen
        intent.putExtra("Settings", getIntent().getExtras());//puts all setting extras in a bundle
        players = sort(players);//sorts the players by highest score to lowest
        int scores[] = new int[players.length];
        Bundle bundle = new Bundle();
        for(int i = 0; i < players.length; i++)
            scores[i] = players[i].getScore();//places the score of each players in an array to be sent over to the win screen
        bundle.putIntArray("playerScores", scores);//places the players score array in the bundle
        String words[] = new String[players.length];
        for(int i = 0; i < words.length; i++)
            words[i] = players[i].getBestWord();//places each players best word in the string array to be sent over to the win screen
        bundle.putStringArray("playerBestWords", words);//places the players best word in the bundle
        for(int i = 0; i < words.length; i++)
            words[i] = players[i].getName();//places each players name in the string array to be sent over to win screen
        bundle.putStringArray("playerNames", words);//places the players name array in the bundle
        for(int i = 0; i < players.length; i++)
            scores[i] = players[i].getHighScore();//places the score of each of the players best word in the int array
        bundle.putIntArray("playerHighScores", scores);//places the array of each players best single word score in the bundle
        words = log.getWorddata();//get the log of each used word in the game and places it in the string array
        bundle.putStringArray("WordRankingsByUSE", words);//places the log of each used word in the bundle
        scores = log.getPointdata();//gets the log of each played words score and places it in the int array
        bundle.putIntArray("ScoreRankingsByUSE", scores);//places the of each played word's score in the bundle
        intent.putExtras(bundle);//places the bundle of this game's information in the intents extras
        startActivity(intent);//switch to win screen
    }

    public Player[] sort(Player[] Ranks)//sort the players by rank using selection sort
    {
        for(int i = 0; i < Ranks.length; i++)
        {
            Player max = Ranks[Ranks.length - 1 - i];
            int pos = Ranks.length - 1 - i;
            for (int j = 0; j < Ranks.length - i - 1; j++)
            {
                if (max.compareTo(Ranks[j]) < 0)
                {
                    pos = j;
                    max = Ranks[j];
                }
            }
            Player temp = Ranks[Ranks.length - i - 1];
            Ranks[Ranks.length - i - 1] = max;
            Ranks[pos] = temp;
        }
        return Ranks;
    }

    public void Roll()
    {
        Dice1.Roll();//rolls the first dice
        Dice2.Roll();//rolls the second dice
    }

    public void Dice1(View view)//onclick for the first dice
    {
        TextView txt = (TextView) findViewById(R.id.Wordical);
        String text = txt.getText().toString()+Dice1.getLetter();
        if(Dice1.getLetter() != '~')
            txt.setText(text);//adds the vowel on the dice to the player's word
        else
            AllVowels();//create pop-up box if it the wild side to display each vowel
    }
    public void Dice2(View view)//onclick for the second dice
    {
        TextView txt = (TextView) findViewById(R.id.Wordical);
        String text = txt.getText().toString()+Dice2.getLetter();
        if(Dice2.getLetter() != '~')
            txt.setText(text);//adds the vowel on the dice to the player's word
        else
            AllVowels();//create pop-up box if it the wild side to display each vowel
    }

    public void AllVowels()//create the screen to display all the vowels and add them to players word
    {
        View.OnClickListener clicked = new View.OnClickListener() {//onclick for each vowel
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) findViewById(R.id.Wordical);
                String t = txt.getText().toString();
                int n = v.getId();//gets the vowel that they clicked
                if(n == 0)
                    t+="a";
                else if(n == 1)
                    t+="e";
                else if(n == 2)
                    t+="i";
                else if(n == 3)
                    t+="o";
                else if(n == 4)
                    t+="u";
                else
                    t+="y";
                txt.setText(t);//add that vowel to the end of the player's word
            }
        };
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {//onclick to exit the dialog box
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder vowels = new AlertDialog.Builder(MainActivity.this);//initializes dialog box to contain all selectable vowels
        vowels.setTitle("Choose a vowel");//sets title of the dialog box
        LinearLayout layout1 = new LinearLayout(MainActivity.this);//creates a linear layout to hold the first row of vowels
        LinearLayout layout2 = new LinearLayout(MainActivity.this);//creates a linear layout to hold the second row of vowels
        LinearLayout whole = new LinearLayout(MainActivity.this);//creates a linear layout to hold the two previous linear layouts
        whole.setGravity(CENTER);//centers the linear layouts within it
        whole.setOrientation(LinearLayout.VERTICAL);//orients the linear layout vertically
        layout1.setVisibility(View.VISIBLE);//make this linear layout visible
        layout1.setOrientation(LinearLayout.HORIZONTAL);//orients the linear layout horizontally
        layout2.setVisibility(View.VISIBLE);//make this linear layout visible
        layout2.setOrientation(LinearLayout.HORIZONTAL);//orients the linear layout horizontally
        layout1.setGravity(CENTER);//centers the items/views within it
        layout2.setGravity(CENTER);//centers the items/views within it
        ImageView aeiou[] = new ImageView[6];//creates an array of imageview to hold each vowel
        for(int i = 0; i < aeiou.length; i++)
        {
            aeiou[i] = new ImageView(MainActivity.this);//initializes the imageview as one in this screen
            if(i == 0)//sets each images to each vowels accordingly
                aeiou[i].setImageResource(R.drawable.a);
            else if(i == 1)
                aeiou[i].setImageResource(R.drawable.e);
            else if(i == 2)
                aeiou[i].setImageResource(R.drawable.i);
            else if(i == 3)
                aeiou[i].setImageResource(R.drawable.o);
            else if(i == 4)
                aeiou[i].setImageResource(R.drawable.u);
            else
                aeiou[i].setImageResource(R.drawable.ydice);
            aeiou[i].setId(i);//sets the id of the imageview uniquely for each vowel
            aeiou[i].setPadding(3,3,3,3);//adds some padding to each imageview
            aeiou[i].setScaleType(ImageView.ScaleType.FIT_CENTER);//sets the scale type of the images so they fit in their layouts
            aeiou[i].setOnClickListener(clicked);//sets the onclick listener to each  imageview so they work when tapped
        }
        for(int i = 0; i < aeiou.length; i++)
        {
            if(i%2 == 0)//adds half of the vowel imageviews to each linear layout
                layout1.addView(aeiou[i]);
            else
                layout2.addView(aeiou[i]);
        }
        whole.addView(layout1);//adds the first linear layout to the grand layout
        whole.addView(layout2);//adds the secnnod linear layout to the grand layout
        vowels.setView(whole);//sets the grand layout as the screen/text of the dialog box
        vowels.setNegativeButton("Done", clickListener);//creates an exit button for the dialog box
        vowels.create();//creates the dialog box
        vowels.show();//displays the dialog box
    }

    public void delete(View view)//subtracts the last letter from the player's word
    {
        TextView txt = (TextView) findViewById(R.id.Wordical);
        String t = txt.getText().toString();
        if(!t.equals(""))//makes sure that there are any letters in the players word to subtract
            txt.setText(t.substring(0, t.length()-1));//subtracts the last letter from the player's word
    }

    public void Hint(View view)//gives the player a hint of a word they can use
    {
        TextView edit = (TextView) findViewById(R.id.Wordical);
        Hint = dictionary.hint(hand, Dice1, Dice2);//gets the hint from the in-game dictionary
        edit.setText("");
        Toast.makeText(getApplicationContext(), Hint, Toast.LENGTH_SHORT).show();
        if(hintCount == 0)
            Hint = "Try (to think of) a " + Hint.length() + " letter word.";//tells the player how many letters are in the word being hinted to
        else if(hintCount == 1)
            Hint = Hint.charAt(0) + " -?- " + Hint.charAt(Hint.length()-1);//gives the player the first and last letter of the word
        else
            Hint = Hint.charAt(0) + " -?- " +  Hint.charAt(Hint.length()/2) + " -?- " + Hint.charAt(Hint.length()-1);//gives the player the first, last, and middle letter of the word
        edit.setHint(Hint);//displays the given hint
        hintCount++;//increase hint helpfulness
        if(hintCount >= 3)//resets to the first hint after 3 clicks
            hintCount = 0;
    }

    public void a(View view)//onclick for the first card
    {
        if(hand[0].getLetter() != '/')//checks if there is a valid card in this position
        {
            TextView edit = (TextView) findViewById(R.id.Wordical);
            String current = edit.getText().toString();
            current+=hand[0].getLetter();
            edit.setText(current);//adds the consonant of this card to their word
        }
    }
    public void b(View view)//onclick for the second card
    {
        if(hand[1].getLetter() != '/')//checks if there is a valid card in this position
        {
            TextView edit = (TextView) findViewById(R.id.Wordical);
            String current = edit.getText().toString();
            current+=hand[1].getLetter();
            edit.setText(current);//adds the consonant of this card to their word
        }
    }
    public void c(View view)//onclick for the third card
    {
        if(hand[2].getLetter() != '/')//checks if there is a valid card in this position
        {
            TextView edit = (TextView) findViewById(R.id.Wordical);
            String current = edit.getText().toString();
            current+=hand[2].getLetter();
            edit.setText(current);//adds the consonant of this card to their word
        }
    }
    public void d(View view)//onclick for the fourth card
    {
        if(hand[3].getLetter() != '/')//checks if there is a valid card in this position
        {
            TextView edit = (TextView) findViewById(R.id.Wordical);
            String current = edit.getText().toString();
            current+=hand[3].getLetter();
            edit.setText(current);//adds the consonant of this card to their word
        }
    }
    public void e(View view)//onclick for the fifth card
    {
        if(hand[4].getLetter() != '/')//checks if there is a valid card in this position
        {
            TextView edit = (TextView) findViewById(R.id.Wordical);
            String current = edit.getText().toString();
            current+=hand[4].getLetter();
            edit.setText(current);//adds the consonant of this card to their word
        }
    }

    public void Rules(View view)//displays the rules and how to play to the player
    {
        DialogInterface.OnClickListener click = new DialogInterface.OnClickListener() {//onclick listener that closes the dialog box
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);//initializes dialog box for the rules
        box.setTitle("Rules");//sets title to "Rules"
        ScrollView scrollView = new ScrollView(MainActivity.this);//creates a scrollable view for all the information so that it fits
        LinearLayout layout = new LinearLayout(MainActivity.this);//creates a linear layout to hold all the information
        TextView rules = new TextView(MainActivity.this);//creates a textview to hold the text for all the rules of the game
        rules.setText(R.string.rules);//sets text to the rules of the game, strings resource folder; string.xml
        TextView Header = new TextView(MainActivity.this);//creates a sub header to indicate the rules on how to play
        Header.setText("How to play");//sets the text of the header
        Header.setGravity(CENTER);//centers the header
        Header.setAllCaps(true);//makes the header all capitals
        Header.setTextSize(25);//sets the header's textsize
        TextView howToplay = new TextView(MainActivity.this);//creates a textview to hold the how to play rules
        howToplay.setText(R.string.gameplay);//sets the text to the rules on how to play, from string.xml
        howToplay.setTextSize(20);//sets the how to play rules textsize
        rules.setTextSize(20);//sets the rules text size
        layout.setOrientation(LinearLayout.VERTICAL);//orients the linear layout vertically
        layout.setGravity(CENTER);//center the linear layout
        layout.addView(rules);//adds the rules text to the linaear layout
        layout.addView(Header);//adds the header text to the linear layout
        layout.addView(howToplay);//adds the how to play rules to th elinear layout
        box.setPositiveButton("Understood", click);//creates an exit buuton for the dialog box
        scrollView.addView(layout);//places the linear layout in the scrollable view
        box.setView(scrollView);//adds the scrollview to the dialog
        box.create();//creates the dialog box
        box.show();//displays the dialog box
    }
}