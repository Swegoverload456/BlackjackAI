package sample;

public class Card {
    int houseNum;
    String houseString;
    int value;
    public Card(){
        houseNum = 0;
        houseString = " ";
        value = 0;
    }
    public void assignVal(int house, int val){
        value = val;
        houseNum = house;
    }
    public int getHouseNum(){
        return houseNum;
    }

    public int getValueNum() {
        return value;
    }
    public String getValueString() {
        String out = "";
        if(value <= 10){
            out = "" + value;
        }
        else{
            if(value == 11){
                out = "Jack";
            }
            else if(value == 12){
                out = "Queen";
            }
            else if(value == 13){
                out = "King";
            }
            else if(value == 14){
                out = "Ace";
            }
        }
        return out;
    }
    public String getHouseString(){
        String out = "";
        if(houseNum == 1) out = "Clubs";
        else if(houseNum == 2) out = "Spades";
        else if(houseNum == 3) out = "Hearts";
        else if(houseNum == 4) out = "Diamonds";
        return out;
    }

}
