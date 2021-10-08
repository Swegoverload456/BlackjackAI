package sample;

public class Bot {
    public Blackjack b;
    double error = 3;
    int dealt[] = new int[13];

    public Bot(){
       for(int i = 0; i < dealt.length; i++){
           dealt[i] = 0;
       }
       b = new Blackjack(5);
    }
    public int playWBot(){
        int out = 0;
        while(!b.t) {
            for(int i = 1; i < b.numOfPlayers; i++){
                countDealt();
                b.pTurn(answer(i), i);
            }
        }
        while(!b.m){
            b.dTurn();
        }
        if(b.win(b.p[1])){
            out = 1;
        }
        else if(b.loss(b.p[1])){
            out = 2;
        }
        else{
            out = 0;
        }
        return out;
    }
    public void playNoBot(){
        b.play();
    }
    public void countDealt(){
        for(int i = 0; i < b.numOfPlayers; i++){
            for(int j = 0; j < b.p[i].getSize(); j++){
                dealt[b.p[i].getValueNum(j)-2]++;
            }
        }
    }
    public boolean answer(int index){
        boolean out = false;
        if(b.handSum(b.p[index]) >= 18 && b.handSum(b.p[index]) < 22){
            out = false;
        }
        else {
            System.out.println("Bad Prob: " + bProbability(index));
            System.out.println("Bad Prob w Error: " + (bProbability(index) * error));
            if ((bProbability(index) * error) < .40) {
                out = true;
            }
        }
        return out;
    }
    public double bProbability(int index){
        double t = 0;
        double nx = 0;
        double nv = 0;
        double sum = 0;
        int c = 12-diff(index);
        if(c > 1 && diff(index) > 2) {
            double p[] = new double[c - 1];
            int threshold = diff(index) + 1;
            for (int i = 0; i < b.numOfPlayers; i++) {
                nv += b.p[i].getSize();
            }
            for (int i = 0; i < p.length; i++) {
                if (threshold + i == 10) {
                    p[i] = 16;
                } else {
                    p[i] = 4;
                }
                nx = dealt[threshold + i - 2];
                sum += p[i] - nx;
            }

            t = sum / (52 - nv);
        }
        else {
            t = (4-dealt[0])/(52-nv);
        }
        return t;
    }
    //Calculate the total difference from 21
    public int diff(int index){
        int out = 21;
        out -= b.handSumLow(b.p[index]);
        return out;
    }
}
