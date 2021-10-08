package sample;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class Blackjack {
    Deck d = new Deck();
    int counter = 0;
    //Including Dealer
    int numOfPlayers;
    boolean t = false;
    public boolean m = false;
    Scanner scan = new Scanner(System.in);
    String in = " ";
    /*
    Dealer is index 0
    Player is index 1
     */
    Player p[];
    public Blackjack(int players){
        d.randomize();
        p = new Player[players];
        numOfPlayers = players;
        populateInPlay();
        deal();
    }
    public void pTurn(boolean h, int index){
        iBJ();
        showHiddenDealer();
        showPlayer();
        if(!t) {
            System.out.println("Would you like to hit or stand? h or s: ");
            //in = scan.nextLine();
            if (h) {
                System.out.println("You chose to hit.");
                hit(p[index]);
            } else if (!h) {
                System.out.println("You chose to stand.");
                t = true;
            } else {
                System.out.println("Your entry was invalid.");
            }
            if(win(p[index])){
                t = true;
            }
            if(totalL(p[index])){
                System.out.println("YOU WENT OVER!!!!");
                t = true;
            }
        }
    }
    public void dTurn(){
        if (handSum(p[0]) < 17) {
            System.out.println("Dealer chose to hit.");
            hit(p[0]);
        }
        else {
            System.out.println("Dealer chose to stand.");
            if (win(p[1])) {
                showAllHandsString();
                System.out.println("YOU WIN!!!!");
            }
            else if (loss(p[1])) {
                showAllHandsString();
                System.out.println("YOU LOST!!!!");
            }
            else{
                showAllHandsString();
                System.out.println("EDGE CASE");
            }
            m = true;
        }

    }
    public void iBJ(){
        //Check for blackjack
        if (instantW(p[1])) {
            showAllHandsString();
            System.out.println("YOU GOT BLACKJACK!!!!");
            t = true;
        }
    }
    public void playGUI(){
        //Check for blackjack
        if (instantW(p[1])) {
            showAllHandsString();
            System.out.println("YOU GOT BLACKJACK!!!!");
            m = true;
        }
        while(!m) {
            showHiddenDealer();
            showPlayer();
            if(!t) {
                System.out.println("Would you like to hit or stand? h or s: ");
                in = scan.nextLine();
                if (in.equals("h")) {
                    System.out.println("You chose to hit.");
                    hit(p[1]);
                    showPlayer();
                } else if (in.equals("s")) {
                    System.out.println("You chose to stand.");
                    t = true;
                } else {
                    System.out.println("Your entry was invalid.");
                }
                if(totalL(p[1])){
                    System.out.println("YOU WENT OVER!!!!");
                    m = true;
                }
            }
            else {
                if (handSum(p[0]) < 17) {
                    System.out.println("Dealer chose to hit.");
                    hit(p[0]);
                }
                else {
                    System.out.println("Dealer chose to stand.");
                    if (win(p[1])) {
                        showAllHandsString();
                        System.out.println("YOU WIN!!!!");
                    }
                    else if (loss(p[1])) {
                        showAllHandsString();
                        System.out.println("YOU LOST!!!!");
                    }
                    m = true;
                }
            }
        }
    }

    public void play(){
        in = " ";
        //Check for blackjack
        if (instantW(p[1])) {
            showAllHandsString();
            System.out.println("YOU GOT BLACKJACK!!!!");
            m = true;
        }
        while(!m) {
            showHiddenDealer();
            showPlayer();
            if(!t) {
                System.out.println("Would you like to hit or stand? h or s: ");
                in = scan.nextLine();
                if (in.equals("h")) {
                    System.out.println("You chose to hit.");
                    hit(p[1]);
                    showPlayer();
                } else if (in.equals("s")) {
                    System.out.println("You chose to stand.");
                    t = true;
                } else {
                    System.out.println("Your entry was invalid.");
                }
                if(totalL(p[1])){
                    System.out.println("YOU WENT OVER!!!!");
                    m = true;
                }
            }
            else {
                if (handSum(p[0]) < 17) {
                    System.out.println("Dealer chose to hit.");
                    hit(p[0]);
                }
                else {
                    System.out.println("Dealer chose to stand.");
                    if (win(p[1])) {
                        showAllHandsString();
                        System.out.println("YOU WIN!!!!");
                    }
                    else if (loss(p[1])) {
                        showAllHandsString();
                        System.out.println("YOU LOST!!!!");
                    }
                    m = true;
                }
            }
        }
    }

    public void deal(){
        for(int i = 0; i < numOfPlayers; i++){
            for(int j = 0; j < 2; j++) {
                p[i].addCard(j, d.getCardHouse(counter), d.getCardValue(counter));
                counter++;
            }
        }
    }
    public boolean instantW(Player pl){
        boolean out = false;
        int t = handSum(pl);
        if(t == 21){
            out = true;
        }
        return out;
    }
    public boolean totalL(Player pl){
        boolean out = false;
        int t = handSum(pl);
        if(t > 21){
            out = true;
        }
        return out;
    }
    public void hit(Player p){
        p.addCard(p.getSize(), d.getCardHouse(counter), d.getCardValue(counter));
        counter++;
    }
    public boolean win(Player pl){
        boolean out = false;
        int t = handSum(pl);
        if(t == 21 && handSum(p[0]) < 21 || (handSum(p[0]) >= 17 && t > handSum(p[0]) && t <= 21) || (handSum(p[0]) > 21 && t <= 21)){
            out = true;
        }
        return out;
    }
    public int handSum(Player p){
        int total = 0;
        int ace = 0;
        for(int i = 0; i < p.getSize(); i++){
            if (p.getValueNum(i) > 10) {
                if (p.getValueNum(i) == 14) {
                    ace++;
                }
                else {
                    total += 10;
                }
            }
            else {
                total += p.getValueNum(i);
            }
        }
        //Optimal ace case computation
        if(ace > 0){
            for(int i = 0; i < ace; i++){
                if(i == 0) {
                    if (total + 11 > 21) {
                        total += 1;
                    } else if (total + 11 <= 21) {
                        total += 11;
                    }
                }
                else{
                    if(total + 11 > 21){
                        if(total + 1 <= 21){
                            total+=1;
                        }
                        else {
                            total -= 9;
                        }
                    }
                    else if(total + 11 <= 21){
                        total+=11;
                    }
                    else{
                        total+=1;
                    }
                }
            }
        }
        return total;
    }
    public int handSumLow(Player p){
        int total = 0;
        int ace = 0;
        for(int i = 0; i < p.getSize(); i++){
            if (p.getValueNum(i) > 10) {
                if (p.getValueNum(i) == 14) {
                    ace++;
                }
                else {
                    total += 10;
                }
            }
            else {
                total += p.getValueNum(i);
            }
        }
        //Suboptimal ace case computation
        if(ace > 0){
            for(int i = 0; i < ace; i++){
                if(i == 0) {
                    if (total + 11 > 21) {
                        total += 11;
                    } else if (total + 11 <= 21) {
                        total += 1;
                    }
                }
                else{
                    if(total + 11 > 21){
                        if(total + 1 <= 21){
                            total-=9;
                        }
                        else {
                            total+=1;
                        }
                    }
                    else if(total + 11 <= 21){
                        total+=1;
                    }
                    else{
                        total+=11;
                    }
                }
            }
        }
        return total;
    }
    public boolean loss(Player pl){
        boolean out = false;
        int t = handSum(pl);
        if(t > 21 || t == handSum(p[0]) || (t < handSum(p[0]) && handSum(p[0]) <= 21)){
            out = true;
        }
        return out;
    }
    public void showAllHandsNum(){
        for(int i = 0; i < numOfPlayers; i++){
            if(i == 0){
                System.out.println("\n" + "Dealer Cards: ");
            }
            else{
                System.out.println("\n" + "Player " + i + " Cards: ");
            }
            for(int j = 0; j < p[i].getSize(); j++){

                System.out.println(p[i].getValueNum(j) + "\t" + "of" + "\t" + p[i].getHouseNum(j));
            }
        }
    }
    public void showHandsNum(){
        for(int i = 1; i < numOfPlayers; i++){
            System.out.println( "\n" + p[i].getValueNum(0) + "\t" + "of" + "\t" + p[i].getHouseNum(0));
            System.out.println(p[i].getValueNum(1) + "\t" + "of" + "\t" + p[i].getHouseNum(1));
        }
    }
    public void showAllHandsString(){
        for(int i = 0; i < numOfPlayers; i++){
            if(i == 0){
                System.out.println("\n" + "Dealer Cards: ");
            }
            else{
                System.out.println("\n" + "Player " + i + " Cards: ");
            }
            for(int j = 0; j < p[i].getSize(); j++){

                System.out.println(p[i].getValueString(j) + "\t" + "of" + "\t" + p[i].getHouseString(j));
            }
        }
    }
    public void showHandsString(){
        for(int i = 1; i < numOfPlayers; i++){
            System.out.println( "\n" + p[i].getValueString(0) + "\t" + "of" + "\t" + p[i].getHouseString(0));
            System.out.println(p[i].getValueString(1) + "\t" + "of" + "\t" + p[i].getHouseString(1));
        }
    }
    public void showPlayer(){
        System.out.println("Your Cards: ");
        for(int i = 0; i < p[1].getSize(); i++){
            show(1, i);
        }
    }
    public void showPlayers(){
        for(int i = 1; i < numOfPlayers; i++) {
            if(i == 1) {
                System.out.println("Your Cards: ");
            }
            else{
                System.out.println("Player " + i + " Cards: ");
            }
            for (int j = 0; j < p[i].getSize(); j++) {
                show(1, j);
            }
        }
    }
    public void showHiddenDealer(){
        System.out.println("\n" + "Dealer Card: ");
        System.out.println("Hidden!");
        for(int i = 1; i < p[0].getSize(); i++){
            show(0, i);
        }
    }
    public void showFullDealer(){
        System.out.println("\n" + "Dealer Card: ");
        for(int i = 0; i < p[0].getSize(); i++){
            show(0, i);
        }
    }
    public void show(int i, int j){
        System.out.println(p[i].getValueString(j) + "\t" + "of" + "\t" + p[i].getHouseString(j));
    }
    private void populateInPlay(){
        for(int i = 0; i < numOfPlayers; i++){
            p[i] = new Player();
        }
    }
    public double calculateProbability(int card){
        if(card > 11){
            card = 11;
        }
        double out;
        int n = 1;
        int m = 2;
        int c[] = {4,8,12,16,20,24,28,32,36,52,52};
        int d = c[card-1];
        int curCardVal = p[0].getValueNum(0);
        if(curCardVal > 10){
            curCardVal = 10;
            if(curCardVal == 14){
                curCardVal = 11;
            }
        }
        if(curCardVal <= card){
            n++;
        }
        for(int i = 1; i < p.length; i++){
            int handSize = p[i].getSize();
            m+=handSize;
            System.out.println(handSize);
            for(int j = 0; j < handSize; j++) {
                curCardVal = p[i].getValueNum(j);
                if(p[i].getValueString(j).equals("Jack") || p[i].getValueString(j).equals("Queen") || p[i].getValueString(j).equals("King") || p[i].getValueString(j).equals("Ace")) {
                    if(!p[i].getValueString(j).equals("Ace")){
                        curCardVal = 10;
                    }
                    else{
                        curCardVal = 1;
                    }
                }
                if (curCardVal <= card) {
                    n++;
                }
            }
        }

        System.out.println("CARD: " + card + "\t\tN: " + n +  "\tD: " + d + "\tM: " + m + "\tL: " + (d-n) + "\tTotal Left: " + (52-m));
        out = ((double)(d-n))/((double)(52-m));
        if(out > 1){
            out = 1;
        }
        BigDecimal bd = new BigDecimal(out*100);
        bd = bd.round(new MathContext(3));
        out = bd.doubleValue();
        return out;
    }
}

class Player{
    Hand h;
    public Player(){
        h = new Hand();
    }
    public void addCard(int index, int house, int value){
        h.addCard(index, house, value);
    }
    public int getHouseNum(int card){
        return h.getHouseNum(card);
    }
    public int getValueNum(int card){
        return h.getValueNum(card);
    }
    public String getHouseString(int card){
        return h.getHouseString(card);
    }
    public String getValueString(int card){
        return h.getValueString(card);
    }
    public int getSize(){
        return h.getSize();
    }
    public void resetCards(){
        h.resetCards();
    }
}

class Hand{
    Card hand[];
    private int size;
    private int capacity;
    public Hand(){
        /*
        Maximum cards a winning blackjack hand can have
        4 aces of any kind
        4 2's of any kind
        3 3's of any kind
         */
        hand = new Card[11];
        for(int i = 0; i < hand.length; i++){
            hand[i] = new Card();
            hand[i].assignVal(0,0);
        }
        size = 0;
        capacity = 8;
    }

    public void addCard(int index, int house, int value){
        if(hand[index].getValueNum() == 0){
            hand[index].assignVal(house, value);
        }
        else{
            System.out.println("Trying to overwrite a current card.");
        }
    }

    public void resetCards(){
        for(int i = 0; i < getSize(); i++){
            hand[i].assignVal(0,0);
        }
    }

    public int getSize(){
        int out = 0;
        for(int i = 0; i < hand.length; i++){
            if(hand[i].getValueNum() == 0){
                i = hand.length;
            }
            else{
                out++;
            }
        }
        return out;
    }

    public int getHouseNum(int card){
        return hand[card].getHouseNum();
    }

    public int getValueNum(int card){
        return hand[card].getValueNum();
    }

    public String getHouseString(int card){
        return hand[card].getHouseString();
    }

    public String getValueString(int card){
        return hand[card].getValueString();
    }

}

