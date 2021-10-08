package sample;

import java.util.Random;

public class Deck {
    Card deck[] = new Card[52];
    int tempHouse = 0;
    int tempVal = 2;
    //Card a = new Card();
    /*public Deck(){
        a.assignVal(1,5);
    }
    public void printDeck(){
        System.out.println(a.getHouseNum() + "\t" + a.getValueNum()());
    }*/
    public Deck() {
        for(int h = 0; h < deck.length; h++){
            deck[h] = new Card();
        }
        for(int i = 0; i < deck.length; i++){
            if(i % 13 == 0){
                tempHouse++;
                tempVal = 2;
            }
            deck[i].assignVal(tempHouse,tempVal);
            tempVal++;
        }
    }
    public Card getCard(int c){
        return deck[c];
    }
    public int getCardValue(int c){
        return deck[c].getValueNum();
    }
    public int getCardHouse(int c){
        return deck[c].getHouseNum();
    }
    public void printNumDeck(){
        for(int i = 0; i < deck.length; i++){
            System.out.println(deck[i].getHouseNum() + "\t" + deck[i].getValueNum());
        }
    }
    public void printStringDeck(){
        for(int i = 0; i < deck.length; i++){
            System.out.println(deck[i].getValueString() + "\t" + "of" + "\t" + deck[i].getHouseString());
        }
    }
    public void randomize(){
        Card tempDeck[] = new Card[deck.length];
        Random r = new Random();
        int c = r.nextInt(deck.length);
        for(int i = 0; i < tempDeck.length; i++){
            tempDeck[i] = new Card();
            tempDeck[i].assignVal(0,0);
        }
        for(int i = 0; i < tempDeck.length; i++){
            while(tempDeck[c].getValueNum() != 0 && tempDeck[c].getHouseNum() != 0) {
                c = r.nextInt(deck.length);
            }
            tempDeck[c] = deck[i];

        }
        deck = tempDeck;
    }

}