package com.example.wordical;

import android.widget.ImageView;

public class Dice {
    private int num;
    private char letter;
    final private String abc = "aeiou~";

    public Dice(){
        num = (int) (Math.random()*6);
        letter = abc.charAt(num);
    }

    public char getLetter() {
        return letter;
    }

    public void Roll(){
        num = (int) (Math.random()*6);
        letter = abc.charAt(num);
    }

    public void setImage(ImageView i)
    {
        if(letter == 'a')
            i.setImageResource(R.drawable.a);
        else if(letter == 'e')
            i.setImageResource(R.drawable.e);
        else if(letter == 'i')
            i.setImageResource(R.drawable.i);
        else if(letter == 'o')
            i.setImageResource(R.drawable.o);
        else if(letter == 'u')
            i.setImageResource(R.drawable.u);
        else
            i.setImageResource(R.drawable.abc);
    }
}
