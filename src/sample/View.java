package sample;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class View {
    private int deckX = 1600;
    private int deckY = 135;
    private int deckTheta = 45;
    private double cardSizeMult = 0.22;
    private int playerPos[][] = new int[7][4];
    private int arrowPos[][] = new int[7][2];
    public String debugging = "";
    private Group view = new Group();
    private TextField xField;
    private TextField yField;
    public Button startB;
    public Button hitB;
    public Button standB;
    public Button formulaB;
    private Label sumLabel;
    private Image table = new Image(new FileInputStream(debugging+"images/table.png"));
    private ImageView tableView = new ImageView(table);
    private Image tableNoB = new Image(new FileInputStream(debugging+"images/tableNoButtons.png"));
    private ImageView tableNoBView = new ImageView(tableNoB);
    private Image formulaImg = new Image(new FileInputStream(debugging+"images/formula.png"));
    private ImageView formulaView = new ImageView(formulaImg);
    private Image newGame = new Image(new FileInputStream(debugging+"images/newGame.png"));
    private ImageView startView = new ImageView(newGame);
    private Image cardBack = new Image(new FileInputStream(debugging+"images/cards_png_zip/purple_back.png"));
    private ImageView cardBackView = new ImageView(cardBack);
    private Image cardImgs[] = new Image[53];
    private ImageView cards[] = new ImageView[53];
    private Image arrow = new Image(new FileInputStream(debugging+"images/arrow.jpg"));
    private ImageView arrowView = new ImageView(arrow);
    private Image win = new Image(new FileInputStream(debugging+"images/win.png"));
    private ImageView winView = new ImageView(win);
    private Image lose = new Image(new FileInputStream(debugging+"images/lose.png"));
    private ImageView loseView = new ImageView(lose);
    private Image standImg[] = new Image[6];
    private ImageView standView[] = new ImageView[standImg.length];
    private Image hitImg[] = new Image[6];
    private ImageView hitView[] = new ImageView[standImg.length];
    private int players = 7;
    private int playerHands[][] = new int[players][11];
    private Controller controller ;
    private Model model ;
    private int countH = 0;
    private int posH = 0;
    private int countS = 0;
    private Blackjack b;
    private Text playerText = new Text();
    private Text playerSum = new Text();
    private Text dealerText = new Text();
    private Text dealerSum = new Text();
    private Text missingCard = new Text();
    private Text probabilityText = new Text();
    private Text probabilityVal = new Text();
    private Text myName = new Text();
    private int winCount = 0;
    private double winRate = 0.0;
    private Text totalWinrate = new Text();
    private boolean formulaToggle = false;

    EventHandler<ActionEvent> hit = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            hit(0);
            boolean lose = b.totalL(b.p[1]);
            if(lose){
                calculateWinrate();
                totalWinrate.setText("Total Winrate: " + winRate + "%");
                totalWinrate.setVisible(true);
            	playerSum.setText(String.valueOf(b.handSum(b.p[1])));
                hideButtons();
                showLose();
                showStart();
                startB.setDisable(false);
            }
            else if(b.handSum(b.p[1]) == 21){
                playerSum.setText(String.valueOf(b.handSum(b.p[1])));
                missingCard.setVisible(false);
                probabilityText.setVisible(false);
                probabilityVal.setVisible(false);
            }
            else{
                updateStats();
            }
            System.out.println("Hand Sum: " + b.handSum(b.p[1]) + " Lost: " + lose);
        }
    };

    EventHandler<ActionEvent> cpuTurns = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            computerTurns();
        }
    };

    EventHandler<ActionEvent> stand = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            System.out.println("Stand");
            hideButtons();
            FadeTransition playerStand = new FadeTransition(Duration.seconds(0.5), standView[0]);
            playerStand.setFromValue(0);
            playerStand.setToValue(1);
            playerStand.setOnFinished(cpuTurns);
            playerStand.play();
        }
    };

    EventHandler<ActionEvent> startGame = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            hideSums();
            hideIndicators();
            if(countS > 0) {
                hideArrow();
                for(int i = 0; i < cards.length; i++){
                    resetCard(cards[i]);
                    cards[i].setOpacity(1);
                    cards[i].setScaleX(1);
                    cards[i].setScaleY(1);
                }
                cards[52].setViewOrder(1);
                for(int i = 0; i < players; i++){
                    for(int j = 0; j < 11; j++){
                        playerHands[i][j] = -1;
                    }
                }
            }
            b = new Blackjack(7);
            startB.setDisable(true);
            hideStart();
            hideWinLose();
            dealCards();
            countS++;;
        }
    };

    EventHandler<ActionEvent> showB = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            moveArrowSlot(0);
            showSums();
            playerSum.setText(String.valueOf(b.handSum(b.p[1])));
            int dealerNum = b.p[0].getValueNum(0);
            if(dealerNum > 10 && dealerNum != 14){
                dealerNum = 10;
            }
            else if(dealerNum == 14){
                dealerNum = 11;
            }
            dealerSum.setText(String.valueOf(dealerNum));
            if(checkInstantW()){
                calculateWinrate();
                showWin();
                showStart();
                startB.setDisable(false);
            }
            else {
                showButtons();
                player1Turn();
                missingCard.setText(String.valueOf(21-b.handSum(b.p[1])));
                probabilityVal.setText((String.valueOf(b.calculateProbability(21-b.handSum(b.p[1])))) + "%");
            }

        }
    };

    EventHandler<ActionEvent> showArrow = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            arrowView.setOpacity(1);
        }
    };

    EventHandler<ActionEvent> showW = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            totalWinrate.setText("Total Winrate: " + winRate + "%");
            totalWinrate.setVisible(true);
            showWin();
            hideButtons();
            showStart();
            startB.setDisable(false);
        }
    };

    EventHandler<ActionEvent> showL = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            totalWinrate.setText("Total Winrate: " + winRate + "%");
            totalWinrate.setVisible(true);
            showLose();
            hideButtons();
            showStart();
            startB.setDisable(false);
        }
    };

    EventHandler<ActionEvent> dealerTotal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            dealerSum.setText(String.valueOf(b.handSum(b.p[0])));
        }
    };

    EventHandler<ActionEvent> formula = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            //showFormula();
            if(!formulaToggle) {
                formulaToggle = true;
                formulaView.setVisible(formulaToggle);
            }
            else if(formulaToggle) {
                formulaToggle = false;
                formulaView.setVisible(formulaToggle);
            }
        }
    };

    public View(Controller controller, Model model) throws FileNotFoundException {

        this.controller = controller;
        this.model = model;
        createAndConfigurePane();

        createAndLayoutControls();

        updateControllerFromListeners();

        observeModelAndUpdateControls();

    }

    public Parent asParent() {
        return view;
    }

    private void observeModelAndUpdateControls() {
        model.xProperty().addListener((obs, oldX, newX) ->
                updateIfNeeded(newX, xField));

        model.yProperty().addListener((obs, oldY, newY) ->
                updateIfNeeded(newY, yField));

        sumLabel.textProperty().bind(model.sumProperty().asString());
        formulaB.setOnAction(formula);
        startB.setOnAction(startGame);
    }

    private boolean checkInstantW(){
        return b.instantW(b.p[1]);
    }

    private void computerTurns(){
        SequentialTransition seq = new SequentialTransition();
        seq.getChildren().add(moveArrowSlotTrans(1));
        for(int i = 1; i < 6; i++) {
            seq.getChildren().add(new PauseTransition(Duration.seconds(1.5)));
            if(b.handSum(b.p[i+1]) == 21 || b.handSumLow(b.p[i+1]) == 21) {
                seq.getChildren().add(moveArrowSlotTrans(i+1));
                FadeTransition cpuStand = new FadeTransition(Duration.seconds(0.5), standView[i]);
                cpuStand.setFromValue(0);
                cpuStand.setToValue(1);
                seq.getChildren().add(cpuStand);
            }
            else if(b.handSum(b.p[i+1]) < 21 || b.handSumLow(b.p[i+1]) < 21){
                for(int j = 0; j < 9; j++){
                    if(b.handSumLow(b.p[i+1]) < 21 && b.handSumLow(b.p[i+1]) != 21) {
                        if (b.calculateProbability(21 - b.handSumLow(b.p[i + 1])) > 51) {
                            seq.getChildren().add(hitTrans(i));
                        }
                        else {
                            FadeTransition cpuStand = new FadeTransition(Duration.seconds(0.5), standView[i]);
                            cpuStand.setFromValue(0);
                            cpuStand.setToValue(1);
                            seq.getChildren().add(cpuStand);
                            j = 9;
                        }
                    }
                    else{
                        FadeTransition cpuStand = new FadeTransition(Duration.seconds(0.5), standView[i]);
                        cpuStand.setFromValue(0);
                        cpuStand.setToValue(1);
                        j = 9;
                    }
                }
            }
        }
        seq.getChildren().add(dealerTurnTrans());
        if (b.win(b.p[1])) {
            seq.setOnFinished(showW);
        } else if (b.loss(b.p[1])) {
            seq.setOnFinished(showL);
        }
        calculateWinrate();
        seq.play();
    }

    private void player1Turn(){
        hitB.setOnAction(hit);
        standB.setOnAction(stand);
    }

    private Transition dealerTurnTrans(){
        SequentialTransition out = new SequentialTransition();
        out.getChildren().add(moveArrowSlotTrans(6));
        out.getChildren().add(new PauseTransition(Duration.seconds(1)));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), cards[52]);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(dealerTotal);
        out.getChildren().add(fadeTransition);
        out.getChildren().add(new PauseTransition(Duration.seconds(1)));
        for(int i = 0; i < 11; i++) {
            if (b.handSum(b.p[0]) < 17) {
                out.getChildren().add(hitTrans(6));
            }
        }
        out.getChildren().add(new PauseTransition(Duration.seconds(2)));
        return out;
    }

    private void calculateWinrate(){
        for(int i = 0; i < 6; i++){
            if(b.win(b.p[i+1])){
                winCount++;
            }
        }
        winRate = ((double)winCount)/(countS*6);
        winRate = (double)Math.round((winRate*100) * 10d) / 10d;
    }

    private void updateIfNeeded(Number value, TextField field) {
        String s = value.toString() ;
        if (! field.getText().equals(s)) {
            field.setText(s);
        }
    }

    private void updateControllerFromListeners() {
        xField.textProperty().addListener((obs, oldText, newText) -> controller.updateX(newText));
        yField.textProperty().addListener((obs, oldText, newText) -> controller.updateY(newText));
    }

    private Transition hitTrans(int player){
        SequentialTransition out = new SequentialTransition();
        out.getChildren().add(new PauseTransition(Duration.seconds(0.5)));
        out.getChildren().add(moveArrowSlotTrans(player));
        int bNum = player+1;
        if(player == 6){
            bNum = 0;
        }
        b.hit(b.p[bNum]);
        int size = handSize(player);
        playerHands[player][size] = cardToInt(b.p[bNum].getValueString(size), b.p[bNum].getHouseString(size));
        ParallelTransition parallelTransition = new ParallelTransition();
        if(player < 6) {
            FadeTransition hitIcon = new FadeTransition();
            hitIcon.setNode(hitView[player]);
            hitIcon.setFromValue(0);
            hitIcon.setToValue(1);
            hitIcon.setDuration(Duration.seconds(1));
            hitIcon.setAutoReverse(true);
            hitIcon.setCycleCount(2);
            parallelTransition.getChildren().add(hitIcon);
        }
        parallelTransition.getChildren().add(moveCardTrans(cards[playerHands[player][size]], playerPos[player][0], playerPos[player][1], playerPos[player][2], true));
        size = handSize(player);
        parallelTransition.getChildren().add(rearrangeCardsTrans(player, size));
        if(bNum == 0){
            parallelTransition.setOnFinished(dealerTotal);
        }
        playerPos[player][3]++;
        out.getChildren().add(parallelTransition);
        return out;
    }

    private void hit(int player){
        moveArrowSlot(player);
        if(player != 6) {
            if(b.handSum(b.p[player + 1]) != 21 && b.handSumLow(b.p[player + 1]) != 21) {
                b.hit(b.p[player + 1]);
                int size = handSize(player);
                playerHands[player][size] = cardToInt(b.p[player + 1].getValueString(size), b.p[player + 1].getHouseString(size));
                moveCard(cards[playerHands[player][size]], playerPos[player][0], playerPos[player][1], playerPos[player][2], true);
                size = handSize(player);
                rearrangeCards(player, size);
                playerPos[player][3]++;
            }
        }
        else{
            b.hit(b.p[0]);
            int size = handSize(6);
            playerHands[6][size] = cardToInt(b.p[0].getValueString(size), b.p[0].getHouseString(size));
            SequentialTransition seq = new SequentialTransition();
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            seq.getChildren().add(moveCardTrans(cards[playerHands[6][size]], playerPos[6][0], playerPos[6][1], playerPos[6][2], true));
            seq.getChildren().add(pause);
            seq.getChildren().add(rearrangeCardsTrans(6, size+1));
            seq.play();
            size = handSize(6);
            rearrangeCards(6, size);
            playerPos[6][3]++;
        }
    }

    private void updateStats(){
        playerSum.setText(String.valueOf(b.handSum(b.p[1])));
        missingCard.setText(String.valueOf(21-b.handSum(b.p[1])));
        probabilityVal.setText((String.valueOf(b.calculateProbability(21-b.handSum(b.p[1])))) + "%");
    }

    private void showStart(){
        startView.setOpacity(1);
    }

    private void hideStart(){
        startView.setOpacity(0);
    }

    private void showWin(){
        winView.setX(667);
        winView.setY(300);
        winView.setOpacity(1);
    }

    private void hideWinLose(){
        winView.setOpacity(0);
        loseView.setOpacity(0);
    }

    private void showLose(){
        loseView.setX(667);
        loseView.setY(300);
        loseView.setOpacity(1);
    }

    private void hideArrow(){
        arrowView.setOpacity(0);
    }

    private void showSums(){
        playerText.setVisible(true);
        dealerText.setVisible(true);
        playerSum.setVisible(true);
        dealerSum.setVisible(true);
        missingCard.setVisible(true);
        probabilityText.setVisible(true);
        probabilityVal.setVisible(true);

    }

    private void hideSums(){
        playerText.setVisible(false);
        dealerText.setVisible(false);
        playerSum.setVisible(false);
        dealerSum.setVisible(false);
        missingCard.setVisible(false);
        probabilityText.setVisible(false);
        probabilityVal.setVisible(false);
    }

    private void hideIndicators(){
        for(int i = 0; i < standView.length; i++){
            standView[i].setOpacity(0);
        }
    }

    private void dealCards(){
        double delay = 0.125;
        SequentialTransition one = new SequentialTransition();
        for(int i = 1; i < b.p.length; i++){
            playerHands[i-1][0] = cardToInt(b.p[i].getValueString(0), b.p[i].getHouseString(0));
            one.getChildren().add(moveCardTrans(cards[playerHands[i-1][0]], playerPos[i-1][0], playerPos[i-1][1], playerPos[i-1][2], true));
            one.getChildren().add(new PauseTransition(Duration.seconds(delay)));
        }
        playerHands[6][0] = cardToInt(b.p[0].getValueString(0), b.p[0].getHouseString(0));
        one.getChildren().add(moveCardTrans(cards[playerHands[6][0]], playerPos[6][0], playerPos[6][1], playerPos[6][2], true));
        one.getChildren().add(new PauseTransition(Duration.seconds(delay)));
        one.play();
        one.stop();
        one.getChildren().removeAll();
        for(int i = 1; i < b.p.length; i++){
            playerHands[i-1][1] = cardToInt(b.p[i].getValueString(1), b.p[i].getHouseString(1));
            one.getChildren().add(moveCardTrans(cards[playerHands[i-1][1]], playerPos[i-1][0], playerPos[i-1][1], playerPos[i-1][2], true));
            one.getChildren().add(rearrangeCardsTrans(i-1, 2));
            one.getChildren().add(new PauseTransition(Duration.seconds(delay)));
        }
        playerHands[6][1] = cardToInt(b.p[0].getValueString(1), b.p[0].getHouseString(1));
        one.getChildren().add(new ParallelTransition(moveCardTrans(cards[playerHands[6][1]], playerPos[6][0], playerPos[6][1], playerPos[6][2], true), moveCardTrans(cards[52], playerPos[6][0], playerPos[6][1], playerPos[6][2], true)));
        one.getChildren().add(rearrangeCardsTrans(6, 2));
        one.getChildren().add(new PauseTransition(Duration.seconds(delay)));
        one.setOnFinished(showB);
        one.play();

    }

    private int cardToInt(String value, String house){
        int out = -1;
        if(value.equals("Jack")){
            out = 9;
        }
        else if(value.equals("Queen")){
            out = 10;
        }
        else if(value.equals("King")){
            out = 11;
        }
        else if(value.equals("Ace")){
            out = 12;
        }
        else {
            out = Integer.parseInt(value) - 2;
        }
        if(!house.equals("Clubs")){
            if(house.equals("Spades")) out += 13;
            else if(house.equals("Hearts")) out += 26;
            else  out += 39;
        }
        return out;
    }

    private void initCards() throws FileNotFoundException {
        arrowView.setFitWidth(100);
        arrowView.setFitHeight(76);
        arrowView.setRotate(90);
        arrowView.setOpacity(0);
        winView.setOpacity(0);
        loseView.setOpacity(0);
        playerPos[0][0] = 220;
        playerPos[0][1] = 385;
        playerPos[0][2] = 43;
        playerPos[0][3] = 0;
        playerPos[1][0] = 450;
        playerPos[1][1] = 550;
        playerPos[1][2] = 27;
        playerPos[1][3] = 0;
        playerPos[2][0] = 717;
        playerPos[2][1] = 633;
        playerPos[2][2] = 7;
        playerPos[2][3] = 0;
        playerPos[3][0] = 1075;
        playerPos[3][1] = 637;
        playerPos[3][2] = -7;
        playerPos[3][3] = 0;
        playerPos[4][0] = 1342;
        playerPos[4][1] = 552;
        playerPos[4][2] = -27;
        playerPos[4][3] = 0;
        playerPos[5][0] = 1572;
        playerPos[5][1] = 387;
        playerPos[5][2] = -43;
        playerPos[5][3] = 0;
        playerPos[6][0] = 875;
        playerPos[6][1] = 125;
        playerPos[6][2] = 0;
        playerPos[6][3] = 0;

        arrowPos[0][0] = 350;
        arrowPos[0][1] = 330;
        arrowPos[1][0] = 550;
        arrowPos[1][1] = 450;
        arrowPos[2][0] = 750;
        arrowPos[2][1] = 540;
        arrowPos[3][0] = 1075;
        arrowPos[3][1] = 540;
        arrowPos[4][0] = 1270;
        arrowPos[4][1] = 450;
        arrowPos[5][0] = 1480;
        arrowPos[5][1] = 330;
        arrowPos[6][0] = 890;
        arrowPos[6][1] = 20;

        int count = 0;
        String h = "";
        String v = "";
        for(int i = 0; i < 4; i++){
            if(i == 0) h = "C";
            else if(i == 1) h = "S";
            else if(i == 2) h = "H";
            else if(i == 3) h = "D";
            for(int j = 2; j < 15; j++){
                if(j == 11) v = "J";
                else if(j == 12) v = "Q";
                else if(j == 13) v = "K";
                else if(j == 14) v = "A";
                else v = String.valueOf(j);
                cardImgs[count] = new Image(new FileInputStream(debugging+"images/cards_png_zip/" + v + h + ".png"));
                cards[count] = new ImageView(cardImgs[count]);
                cards[count].setX(deckX);
                cards[count].setY(deckY);
                cards[count].setRotate(deckTheta);
                cards[count].setFitHeight(1056*cardSizeMult);
                cards[count].setFitWidth(691*cardSizeMult);
                count++;
            }
        }
        for(int i = count; i < cards.length; i++){
            cards[i] = new ImageView(cardBack);
            cards[i].setX(deckX);
            cards[i].setY(deckY);
            cards[i].setRotate(deckTheta);
            cards[i].setFitHeight(1056*cardSizeMult);
            cards[i].setFitWidth(691*cardSizeMult);
        }
        for(int i = 0; i < players; i++){
            for(int j = 0; j < 11; j++){
                playerHands[i][j] = -1;
            }
        }
        for(int i = 0; i < standImg.length; i++){
            standImg[i] = new Image(new FileInputStream(debugging+"images/standImage.png"));
            hitImg[i] = new Image(new FileInputStream(debugging+"images/hitImage.png"));
            standView[i] = new ImageView(standImg[i]);
            hitView[i] = new ImageView(hitImg[i]);
            standView[i].setFitHeight(150);
            standView[i].setFitWidth(150);
            standView[i].setViewOrder(1);
            standView[i].setOpacity(0);
            view.getChildren().add(standView[i]);
            hitView[i].setFitHeight(150);
            hitView[i].setFitWidth(150);
            hitView[i].setViewOrder(1);
            hitView[i].setOpacity(0);
            view.getChildren().add(hitView[i]);
        }
        standView[0].setX(playerPos[0][0]-125);
        standView[0].setY(playerPos[0][1]+135);
        standView[0].setRotate(playerPos[0][2]);
        standView[1].setX(playerPos[1][0]-75);
        standView[1].setY(playerPos[1][1]+175);
        standView[1].setRotate(playerPos[1][2]);
        standView[2].setX(playerPos[2][0]-25);
        standView[2].setY(playerPos[2][1]+190);
        standView[2].setRotate(playerPos[2][2]);
        standView[3].setX(playerPos[3][0]+25);
        standView[3].setY(playerPos[3][1]+190);
        standView[3].setRotate(playerPos[3][2]);
        standView[4].setX(playerPos[4][0]+75);
        standView[4].setY(playerPos[4][1]+175);
        standView[4].setRotate(playerPos[4][2]);
        standView[5].setX(playerPos[5][0]+125);
        standView[5].setY(playerPos[5][1]+135);
        standView[5].setRotate(playerPos[5][2]);
        for(int i = 0; i < hitView.length; i++){
            hitView[i].setX(standView[i].getX());
            hitView[i].setY(standView[i].getY());
            hitView[i].setRotate(standView[i].getRotate()-13);
        }
    }

    private void showButtons(){
        tableNoBView.setOpacity(0);
        hitB.setDisable(false);
        standB.setDisable(false);
    }

    private void hideButtons(){
        tableNoBView.setOpacity(1);
        hitB.setDisable(true);
        standB.setDisable(true);
    }

    private void createAndLayoutControls() throws FileNotFoundException {
        initCards();
        tableView.setX(0);
        tableView.setY(0);
        tableView.setFitHeight(1080);
        tableView.setFitWidth(1920);
        tableView.setViewOrder(100);
        tableNoBView.setX(0);
        tableNoBView.setY(0);
        tableNoBView.setFitHeight(1080);
        tableNoBView.setFitWidth(1920);
        tableNoBView.setViewOrder(99);
        formulaView.setX(0);
        formulaView.setY(0);
        formulaView.setFitHeight(1080);
        formulaView.setFitWidth(1920);
        startView.setX(770);
        startView.setY(900);
        startView.setFitHeight(152);
        startView.setFitWidth(400);
        startView.setOpacity(1);
        startView.setViewOrder(2);
        cardBackView.setX(deckX);
        cardBackView.setY(deckY);
        cardBackView.setRotate(deckTheta);
        cardBackView.setFitHeight(1056*cardSizeMult);
        cardBackView.setFitWidth(691*cardSizeMult);
        tableView.setPreserveRatio(true);
        cardBackView.setPreserveRatio(true);
        view.getChildren().add(tableView);
        for(int i  = 0; i < cards.length; i++){
            view.getChildren().add(cards[i]);
        }
        view.getChildren().add(cardBackView);
        xField = new TextField();
        xField.setLayoutX(500);
        xField.setLayoutY(500);
        configTextFieldForInts(xField);
        yField = new TextField();
        yField.setLayoutX(500);
        yField.setLayoutY(700);
        configTextFieldForInts(yField);
        sumLabel = new Label();
        sumLabel.setLayoutX(500);
        sumLabel.setLayoutY(800);
        startB = new Button();
        startB.setLayoutX(800);
        startB.setLayoutY(930);
        startB.setPrefSize(340, 100);
        startB.setOpacity(0);
        startB.setViewOrder(1);
        hitB = new Button();
        hitB.setLayoutX(780);
        hitB.setLayoutY(910);
        hitB.setPrefSize(150, 150);
        hitB.setOpacity(0);
        standB = new Button();
        standB.setLayoutX(990);
        standB.setLayoutY(910);
        standB.setPrefSize(150, 150);
        standB.setOpacity(0);
        formulaB = new Button();
        formulaB.setLayoutX(1720);
        formulaB.setLayoutY(800);
        formulaB.setPrefSize(175,125);
        formulaB.setOpacity(0);
        formulaB.setViewOrder(-1);
        playerText.setX(75);
        playerText.setY(75);
        playerText.setText("Your Total Sum is: ");
        playerText.setStyle("-fx-font: 24 arial;");
        playerText.setVisible(false);
        playerText.setViewOrder(1);
        playerSum.setX(280);
        playerSum.setY(75);
        playerSum.setStyle("-fx-font: 24 arial;");
        playerSum.setFill(Paint.valueOf("lightblue"));
        playerSum.setViewOrder(1);
        dealerText.setX(1100);
        dealerText.setY(75);
        dealerText.setText("Dealer's Total Sum is: ");
        dealerText.setStyle("-fx-font: 24 arial;");
        dealerText.setVisible(false);
        dealerText.setViewOrder(1);
        dealerSum.setX(1340);
        dealerSum.setY(75);
        dealerSum.setStyle("-fx-font: 24 arial;");
        dealerSum.setFill(Paint.valueOf("red"));
        dealerSum.setViewOrder(1);
        probabilityText.setX(75);
        probabilityText.setY(175);
        probabilityText.setText("Your Probability of getting a \nor lower: ");
        probabilityText.setStyle("-fx-text-fill: black;-fx-font: 24 arial;");
        probabilityText.setViewOrder(2);
        probabilityText.setVisible(false);
        missingCard.setX(380);
        missingCard.setY(175);
        missingCard.setStyle("-fx-font: 24 arial;");
        missingCard.setFill(Paint.valueOf("orange"));
        missingCard.setViewOrder(1);
        missingCard.setVisible(false);
        probabilityVal.setX(170);
        probabilityVal.setY(203);
        probabilityVal.setStyle("-fx-font: 24 arial;");
        probabilityVal.setFill(Paint.valueOf("lightblue"));
        probabilityVal.setViewOrder(1);
        probabilityVal.setVisible(false);
        myName.setText("This program was made by: Juan Velasquez");
        myName.setX(50);
        myName.setY(1000);
        myName.setStyle("-fx-font: 24 arial;");
        myName.setFill(Paint.valueOf("white"));
        myName.setViewOrder(-2);
        totalWinrate.setX(1500);
        totalWinrate.setY(1000);
        totalWinrate.setStyle("-fx-font: 24 arial;");
        totalWinrate.setFill(Paint.valueOf("white"));
        totalWinrate.setViewOrder(1);
        totalWinrate.setVisible(true);
        formulaView.setViewOrder(0);
        formulaView.setVisible(false);
        view.getChildren().add(arrowView);
        view.getChildren().add(winView);
        view.getChildren().add(loseView);
        view.getChildren().add(startB);
        view.getChildren().add(hitB);
        view.getChildren().add(standB);
        view.getChildren().add(formulaB);
        view.getChildren().add(startView);
        view.getChildren().add(tableNoBView);
        view.getChildren().add(playerText);
        view.getChildren().add(playerSum);
        view.getChildren().add(dealerText);
        view.getChildren().add(dealerSum);
        view.getChildren().add(missingCard);
        view.getChildren().add(probabilityText);
        view.getChildren().add(probabilityVal);
        view.getChildren().add(myName);
        view.getChildren().add(totalWinrate);
        view.getChildren().add(formulaView);
        hideButtons();
    }

    private void createAndConfigurePane() {

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setHalignment(HPos.RIGHT);
        leftCol.setHgrow(Priority.NEVER);

        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.SOMETIMES);

    }

    private void configTextFieldForInts(TextField field) {
        field.setTextFormatter(new TextFormatter<Integer>((Change c) -> {
            if (c.getControlNewText().matches("-?\\d*")) {
                return c ;
            }
            return null ;
        }));
    }

    private int handSize(int player){
        int c = 0;
        for(int i = 0; i < 11; i++){
            if(playerHands[player][i] != -1){
                c++;
            }
        }
        return c;
    }

    private void rearrangeCards(int player, int handSize){
        int maxSpace = 150;
        int spaceInterval = maxSpace/handSize;
        int x = playerPos[player][0]-(maxSpace/2);
        int y = playerPos[player][1]-(maxSpace/2);
        int theta = playerPos[player][2];
        if(player == 6){
            for (int i = 0; i < handSize; i++) {
                cards[playerHands[player][i]].setViewOrder(handSize - i+1);
                moveCard(cards[playerHands[player][i]], x, y, theta, false);
                if(i == 1) {
                    cards[52].setViewOrder(handSize - i-0.1+1);
                    moveCard(cards[52], x, y, theta, false);
                }
                x += spaceInterval;
                y += spaceInterval;
            }
        }
        else {
            for (int i = 0; i < handSize; i++) {
                cards[playerHands[player][i]].setViewOrder(handSize - i+1);
                moveCard(cards[playerHands[player][i]], x, y, theta, false);
                x += spaceInterval;
                y += spaceInterval;
            }
        }
    }

    private Transition rearrangeCardsTrans(int player, int handSize){
        int maxSpace = 150;
        int spaceInterval = maxSpace/handSize;
        int x = playerPos[player][0]-(maxSpace/2);
        int y = playerPos[player][1]-(maxSpace/2);
        int theta = playerPos[player][2];
        SequentialTransition out = new SequentialTransition();
        for(int i = 0; i < handSize; i++) {
            cards[playerHands[player][i]].setViewOrder(handSize-i+1);
            out.getChildren().add(moveCardTrans(cards[playerHands[player][i]], x, y, theta, false));
            x += spaceInterval;
            y += spaceInterval;
        }
        return out;
    }

    private void moveArrowSlot(int player){
        moveArrow(arrowPos[player][0], arrowPos[player][1], playerPos[player][2]);
    }

    private Transition moveArrowSlotTrans(int player){
        return moveArrowTrans(arrowPos[player][0], arrowPos[player][1], playerPos[player][2]);
    }

    private void moveArrow(int x, int y, int theta){
        TranslateTransition t = new TranslateTransition();
        double time = (1.0);
        t.setDuration(Duration.seconds(time));
        t.setToX(x);
        t.setToY(y);
        t.setNode(arrowView);
        RotateTransition r = new RotateTransition();
        r.setDuration(Duration.seconds(time));
        r.setToAngle(theta+90);
        r.setNode(arrowView);
        ParallelTransition p = new ParallelTransition(t, r);
        p.setOnFinished(showArrow);
        p.play();
    }

    private Transition moveArrowTrans(int x, int y, int theta){
        TranslateTransition t = new TranslateTransition();
        double time = (1.0);
        t.setDuration(Duration.seconds(time));
        t.setToX(x);
        t.setToY(y);
        t.setNode(arrowView);
        RotateTransition r = new RotateTransition();
        r.setDuration(Duration.seconds(time));
        r.setToAngle(theta+90);
        r.setNode(arrowView);
        ParallelTransition p = new ParallelTransition(t, r);
        p.setOnFinished(showArrow);
        return p;
    }

    private void resetCard(ImageView card){
        TranslateTransition t = new TranslateTransition();
        double time = 0.25;
        t.setDuration(Duration.seconds(time));
        t.setToX(0);
        t.setToY(0);
        t.setNode(card);
        RotateTransition r = new RotateTransition();
        r.setDuration(Duration.seconds(time));
        r.setToAngle(deckTheta);
        r.setNode(card);
        ParallelTransition p = new ParallelTransition(t, r);
        p.play();
    }

    private void moveCard(ImageView pic, int x, int y, int theta, boolean resize){
        TranslateTransition t = new TranslateTransition();
        double scale = -0.19;
        double tConst = 0.00245399;
        double speed = 1.85;
        double time = ((Math.abs(x-deckX) + Math.abs(y-deckY))/2)*tConst*(2-speed);
        t.setDuration(Duration.seconds(time));
        t.setToX(x-deckX-12);
        t.setToY(y-deckY-12);
        t.setNode(pic);
        RotateTransition r = new RotateTransition();
        r.setDuration(Duration.seconds(time));
        r.setToAngle(theta);
        r.setNode(pic);
        ScaleTransition s = new ScaleTransition();
        s.setDuration(Duration.seconds(time));
        s.setByX(scale);
        s.setByY(scale);
        s.setNode(pic);
        if(resize) {
            ParallelTransition p = new ParallelTransition(t, r, s);
            p.play();
        }
        else{
            ParallelTransition p = new ParallelTransition(t, r);
            p.play();
        }
    }

    private Transition moveCardTrans(ImageView pic, int x, int y, int theta, boolean resize){
        TranslateTransition t = new TranslateTransition();
        double scale = -0.19;
        double tConst = 0.00245399;
        double speed = 1.85;
        double time = ((Math.abs(x-deckX) + Math.abs(y-deckY))/2)*tConst*(2-speed);
        t.setDuration(Duration.seconds(time));
        t.setToX(x-deckX-12);
        t.setToY(y-deckY-12);
        t.setNode(pic);
        RotateTransition r = new RotateTransition();
        r.setDuration(Duration.seconds(time));
        r.setToAngle(theta);
        r.setNode(pic);
        ScaleTransition s = new ScaleTransition();
        s.setDuration(Duration.seconds(time));
        s.setByX(scale);
        s.setByY(scale);
        s.setNode(pic);
        Transition out;
        if(resize) {
            ParallelTransition p = new ParallelTransition(t, r, s);
            out = p;
        }
        else{
            ParallelTransition p = new ParallelTransition(t, r);
            out = p;
        }

        return out;
    }
}