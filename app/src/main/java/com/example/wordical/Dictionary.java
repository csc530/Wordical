package com.example.wordical;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

public class Dictionary {
    public static final String mPath = "Dictionary.txt";//path to the dictionary text file
    private String Dictionary[];//array to hold all the words in the dictionary textfile
    private Context context;//the context/intent of game's current position

    public Dictionary(Context circumstance){
        Scanner read;//sets up scanner to read in the textfile
        this.context = circumstance;//gets the context of the game
        AssetManager am = context.getAssets();//gets assests from this game's context, ie.the assets folder
        try {
            InputStream is = am.open(mPath);//opens up the path to the dictionary textfile and initialiazes an inputstream/input from this file
            read = new Scanner(is);//initializes the scanner to read in from the inputstream
            int i = 0;//will hold the size of the dictionary
            Stack <String> s = new Stack<String>();//sets up a temporary stack hold the contents of the dictionary
            while(read.hasNext())//will read in the next line of the dictionary to the stack while there is something to read
            {
                s.push(read.nextLine());
                i++;
            }
            read.close();//closes the scanner
            int size = i;
            i = 0;
            Dictionary = new String[size];//formats the string array to the size of the dictionary
            while(!s.isEmpty())//while the stack is not empty transker the contents from the stack to the array
            {
                Dictionary[size-1-i] = s.pop();//places the contents of the stack in alphabetical order from a-z
                i++;
            }
        }
        catch (Exception e)//catch if there is an error
        {
            e.printStackTrace();
            Broken();
        }
    }

    private void Broken() {//runs this if the dictionary textfile could not be found or something else went wrong allowing the game to not crash
        //this will allow all dictionary functions work although very poorly with no real use
        Dictionary = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    }

    public boolean Find(String word){//check if a certain word is in the dictionary
        return BinarySearch(0, Dictionary.length, word) >= 0;
    }

    private int BinarySearch(int begin, int finish, String w)
    {//searches for a word in the dictionary array using binary search
        int mid = (begin + finish)/2;
        if(mid >= Dictionary.length || begin > finish)
            return -1;
        else
        {
            if (Dictionary[mid].equalsIgnoreCase(w))
                return mid;
            else
            {
                if (Dictionary[mid].compareToIgnoreCase(w) < 0)
                    return BinarySearch(mid + 1, finish, w);
                else
                    return BinarySearch(begin, mid - 1, w);
            }
        }
    }

    private int LinearSearch(int start, String word)
    {//searches for a word in the dictionary using linear search
        boolean done = false;
        String compared;
        while(!done)
        {
            int j = 0;
            boolean match = true;
            compared = Dictionary[start];
            while(j < compared.length() && match)
            {
                if(word.indexOf(compared.charAt(j)) == -1)
                    match = false;
                j++;
            }
            if(match)
                done = true;
            else if(start == Dictionary.length)
            {
                start = -1;
                done = true;
            }
            else
                start++;
        }
        return start;
    }

    public String hint(Card cards[], Dice d1, Dice d2){//searches for a possible word with the given letters available to the player
        String Possible;//converts the objects to a string of letters that they contain
        if(d1.getLetter() == '~' || d2.getLetter() == '~')
            Possible = "" + cards[0].getLetter() + cards[1].getLetter() + cards[2].getLetter() + cards[3].getLetter() + cards[4].getLetter() + "aeiouy";
        else
            Possible = "" + cards[0].getLetter() + cards[1].getLetter() + cards[2].getLetter() + cards[3].getLetter() + cards[4].getLetter() + d1.getLetter() + d2.getLetter();
        int i = LinearSearch(0, Possible);//searches for a possible word
        if(i != -1)
            Possible = Dictionary[i];
        else
            Possible = "-1";
        return Possible;
    }
}