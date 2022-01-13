package com.example.wordical;

import android.widget.ImageView;

public class Card {
    private int Points;//sets up instance variable of the card's points
    private char Letter;//sets up instance variable of the card's letter

    public Card(char letter, int value){//constructor for this object declaring this card's letter and point worth
        Points = value;
        Letter = letter;
    }

    public int getPoints(){//returns the card's value
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public char getLetter(){//returns the card's letter
        return Letter;
    }

    public void setLetter(char letter) {//changes/sets letter of the card
        Letter = letter;
    }

    public boolean equals(Card card) {
        return (card.getLetter() == Letter && card.getPoints() == Points);
    }

    public int compareToChar(Card card){
        char letter = card.getLetter();
        if(Letter == letter)
            return 0;
        else if(Letter > letter)
            return 1;
        else
            return -1;
    }

    public int compareToValue(Card card){
        int value = card.getPoints();
        if(Points > value)
            return 1;
        else if(Points < value)
            return -1;
        else
            return 0;
    }

    public void setImage(ImageView i) {//sets the image based on the card's letter
            if (Letter == 'b')
                i.setImageResource(R.drawable.b);
            else if (Letter == 'c')
                i.setImageResource(R.drawable.c);
            else if (Letter == 'd')
                i.setImageResource(R.drawable.d);
            else if (Letter == 'f')
                i.setImageResource(R.drawable.f);
            else if (Letter == 'g')
                i.setImageResource(R.drawable.g);
            else if (Letter == 'h')
                i.setImageResource(R.drawable.h);
            else if (Letter == 'j')
                i.setImageResource(R.drawable.j);
            else if (Letter == 'k')
                i.setImageResource(R.drawable.k);
            else if (Letter == 'l')
                i.setImageResource(R.drawable.l);
            else if (Letter == 'm')
                i.setImageResource(R.drawable.m);
            else if (Letter == 'n')
                i.setImageResource(R.drawable.n);
            else if (Letter == 'p')
                i.setImageResource(R.drawable.p);
            else if (Letter == 'q')
                i.setImageResource(R.drawable.q);
            else if (Letter == 'r')
                i.setImageResource(R.drawable.r);
            else if (Letter == 's')
                i.setImageResource(R.drawable.s);
            else if (Letter == 't')
                i.setImageResource(R.drawable.t);
            else if (Letter == 'v')
                i.setImageResource(R.drawable.v);
            else if (Letter == 'w')
                i.setImageResource(R.drawable.w);
            else if (Letter == 'x')
                i.setImageResource(R.drawable.x);
            else if (Letter == 'y')
                i.setImageResource(R.drawable.y);
            else if (Letter == 'z')
                i.setImageResource(R.drawable.z);
            else
                i.setImageResource(R.drawable.wordical);
    }

    @Override
    public String toString() {//returns the letter of the card
        return "Letter: " + Letter;
    }
}