package com.example.wordical;

public class Deck {
    private int count;
    //TO DO: make the length that matches your number of cards
    private Card data[];

    public Deck(char deckSize) {//contructor for the deck class initializing the count and the size of the deck
        count = 0;
        create(deckSize);
    }

    private void create(char ls) {
        double n;
        if(ls == 'L')//creates a large deck
        {
            n = 1;
            data = new Card[108];
        }
        else//creates a small deck
        {
            n = 2;
            data = new Card[52];
        }
        int Ratio[] = {4,4,7,4,6,4,3,4,8,3,10,4,3,10,8,10,4,4,2,4,2};//holds the amount of each letter in the deck
        char letter[] = {'b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','y','z'};///each letter in the deck
        int Points[] = {3,3,2,4,2,4,8,5,1,3,1,3,10,1,1,1,4,4,8,4,10};//the value of each letter
        for(int c = 0; c < Ratio.length; c++)
        {
            int num = (int) (Ratio[c]*n);
            for(int i = 0; i < num; i++)
                push(new Card(letter[c], Points[c]));//adds a card for each amount of each constonnt needed on the deck
        }
        shuffle();//shuffles the deck
    }

    public void shuffle(){//shuffles the deck
        for(int i = 0; i < count; i++)//cycles through deck
        {//swaps to cards random cards in the deck
            int r1 = (int) (Math.random() * count);
            int r2 = (int) (Math.random() * count);
            Card temp = data[r1];
            data[r1] = data[r2];
            data[r2] = temp;
        }
    }

    public void push(Card addMe) {//adds new cards to the deck
        data[count] = addMe;
        if(count < data.length-1)
            count++;
    }
    public int size() {//return the size of the deck
        return count;
    }
    public Card pop() {//returns the card at the top of the deck
        if(count > 0)
            count--;
        return data[count];
    }
}