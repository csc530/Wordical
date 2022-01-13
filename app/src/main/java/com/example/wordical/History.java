package com.example.wordical;

public class History {
    private String Worddata[];//sets up the instance variable for each added word
    private int Pointdata[];//sets up the instance variable for each added word's score
    private String save[];//sets up the instance variable for each added word in sequential order
    private int position;//sets up the instance variable for the position of the next word and score

    public History(){//empty contructor to create a new history object
        Worddata = new String [100];
        Pointdata = new int[100];
        save = new String[100];
        position = 0;
    }

    public History(int a[], String b[]){//constructor to create a history with given data
        Worddata = new String [a.length+100];
        Pointdata = new int[a.length+100];
        save = new String[a.length+100];
        for(int i = 0; i < a.length; i++)
        {
            Worddata[i] = b[i];
            Pointdata[i] = a[i];
            save[i] = b[i];
        }
        position = a.length;
    }

    public void sortByscore(){//sorts the history by score using selection sort
        for(int i = 0; i < position; i++)
        {
            int left = position-i-1;
            int pos = left;
            int max = Pointdata[left];
            for(int j = 0; j < left; j++)
            {
                if(max < Pointdata[j])
                {
                    pos = j;
                    max = Pointdata[j];
                }
            }
            swap(pos, left);
        }
    }

    public void sortByUse(){
        for(int i = 0; i < position; i++)
        {
            boolean found  = false;
            int j = 0;
            while(!found)
            {
                if(save[i].equals(Worddata[j]))
                {
                    found = true;
                    swap(i, j);
                }
            }
        }
    }

    private void swap(int i, int j){//swaps to elements in the history
        String string = Worddata[j];
        int in = Pointdata[j];
        Worddata[j] = Worddata[i];
        Worddata[i] = string;
        Pointdata[j] = Pointdata[i];
        Pointdata[i] = in;
    }

    public void add(String word, int points){//adds a new word with its score to the history
        if(word.equals(""))
            word = "Loss turn";
        Worddata[position] = word;
        Pointdata[position] = points;
        save[position] = word;
        if(position < Worddata.length)
            position++;
    }

    public boolean isUsed(String word){//checks if a word has been used before using linear search
        boolean found = false;
        int i = 0;
        while(!found && i < position)
        {
            if(Worddata[i].equals(word))
                found = true;
            i++;
        }
        return found;
    }

    public String print(){//throws out the last element in the history
        if(position > 0)
            position--;
        return Worddata[position] + ": " + Pointdata[position] + "WP";
    }

    public String print(int i){//prints out a specific element in the history
        if(i >= 0 && i < save.length)
            return Worddata[i] + ": " + Pointdata[i] + "WP\n";
        else
            return null;
    }

    public int size(){//return the size of the history
        return position;
    }

    public int[] getPointdata() {//return the score of each word in the history in int array
        int a[] = new int[position];
        for(int i = 0; i < position; i++)
            a[i] = Pointdata[i];
        return a;
    }

    public String[] getWorddata() {//returns each word in the history in a string array
        String a[] = new String[position];
        for(int i = 0; i < position; i++)
            a[i] = Worddata[i];
        return a;
    }
}