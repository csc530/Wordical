package com.example.wordical;

public class Player {
    private String Name;//sets up the instance variable for the player's name
    private int Score;//sets up the instance variable for the player's score
    private String BestWord;//sets up the instance variable for the player's Best played word
    private int HighScore;//sets up the instance variable for the score of the player's bestword

    public Player(String name){//the construcot for the player object initializing their name
        Name = name;
        Score = 0;
        HighScore = -1;
        BestWord = "";
    }

    public String getName() {//return the name of the player
        return Name;
    }

    public void setName(String name) {//
        Name = name;
    }

    public int getScore() {//returns the current score the player
        return Score;
    }

    public void addToScore(int Points){//adds the points to the player's current score
        Score+=Points;
    }

    public int compareTo(Player ply){//compares two player object by their score's
        int n = ply.getScore();
        if(Score > n)
            return 1;
        else if(Score < n)
            return -1;
        else
            return 0;
    }

    public int getHighScore() {//returns the score for the player's best word
        return HighScore;
    }

    public String getBestWord() {//returns the best played word of the player
        return BestWord;
    }

    public void isBestWord(String word, int Points)
    {//determines if a word is the player's highest played word and if it is sets it as their bestword along with it's score
        if(Points > HighScore)
        {
            BestWord = word;
            HighScore = Points;
        }
    }

  //@Override
    public String toString() {
        return "Player: " +
                Name +
                "\tScore =" + Score +
                "\n" + Name + "'s Best Word was: \"" + BestWord + '\"' +
                "played for " + HighScore +
                "WP";
    }
}