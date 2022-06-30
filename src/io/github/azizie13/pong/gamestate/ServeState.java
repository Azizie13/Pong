package io.github.azizie13.pong.gamestate;

import io.github.azizie13.pong.entities.Ball;
import io.github.azizie13.pong.entities.Powerup;
import io.github.azizie13.pong.gui.PongPanel;
import io.github.azizie13.pong.gamelogic.States;

import java.awt.*;

public class ServeState extends GameState {
    public static final float X_SPEED_FACTOR = 1.009f;
    public static final float Y_SPEED_FACTOR = 1.005f;

//    public static final float X_SPEED_FACTOR = 1.0f;
//    public static final float Y_SPEED_FACTOR = 1.0f;

    public static final int MIN_BALL_SIZE = 25;
    public static float DEFAULT_BALL_SIZE_FACTOR = 0.9f;

    private static float BALL_SIZE_FACTOR = DEFAULT_BALL_SIZE_FACTOR;
    private static final int WINNER_SCORE = 11;

    private static boolean AIFlag1 = false, AIFlag2 = false;
    public static int powerupFlag = 0;

    public ServeState(String stateName) {
        super(stateName);
    }

    public void update() {
        this.updateAI();

        if(powerupFlag != 0)
            this.summonPowerup();

        this.collisionDetection();

        if(Powerup.cloneFlag){
            Powerup.cloneFlag = false;
            Ball.addClones();
        }

        this.checkWinCondition();
        Ball.clearBall();

        if(PongPanel.mouseClicked){
            System.out.println(PongPanel.mousePos.getSize());
        }
    }

    private void summonPowerup(){
        if(Powerup.counter <= 0){
            //Kill current powerup
            PongPanel.powerup = null;

            int randomX = PongPanel.random.nextInt(400) - 200;
            int randomY = PongPanel.random.nextInt(400) - 200;

            //Summon Powerup
            PongPanel.powerup = new Powerup(new Rectangle(PongPanel.GAME_WIDTH/2 + randomX, PongPanel.GAME_HEIGHT /2 + randomY, 30,30), powerupFlag);
            PongPanel.powerup.setColor(Powerup.colorMap.get(PongPanel.powerup.id));

            //Setup Spawn Timer
            Powerup.spawnTimer = PongPanel.random.nextInt(30) * 30;
            Powerup.counter = Powerup.spawnTimer;
        }else
            Powerup.counter--;
    }

    private void checkWinCondition() {
        if(PongPanel.balls.toArray().length == 1){
            Ball ball = PongPanel.balls.get(0);
            if(ball.x <= 0){
                PongPanel.score.player2Score();

                if(PongPanel.score.getScore(2) >= WINNER_SCORE){
                    PongPanel.score.setWinner(2);
                    States.changeState("over");
                }else{
                    States.changeState("title");
                }
            }

            if(ball.x >= PongPanel.GAME_WIDTH - ball.width){
                PongPanel.score.player1Score();

                if(PongPanel.score.getScore(1) >= WINNER_SCORE){
                    PongPanel.score.setWinner(1);
                    States.changeState("over");
                }else{
                    States.changeState("title");
                }
            }
        }
    }

    private void updateAI() {
        if(AIFlag1 && !AIFlag2) // PvE Left
        {
            PongPanel.paddle1.aiControl();
            PongPanel.paddle1.getAiControl().setPerfectFlag(false);
        }
        else if(!AIFlag1)
            PongPanel.paddle1.move();

        if(AIFlag2 && !AIFlag1)  //PvE Right
        {
            PongPanel.paddle2.aiControl();
            PongPanel.paddle2.getAiControl().setPerfectFlag(false);
        }
        else if(!AIFlag2)
            PongPanel.paddle2.move();

        if(AIFlag1 && AIFlag2)  //AI vs AI
        {
            PongPanel.paddle1.aiControl();
            PongPanel.paddle2.aiControl();

            PongPanel.paddle1.getAiControl().setPerfectFlag(true);
            PongPanel.paddle2.getAiControl().setPerfectFlag(true);
        }
    }

    private void collisionDetection(){
        for(Ball ball : PongPanel.balls){
            if(ball != null){
                ball.move();
                if(ball.collide(PongPanel.paddle1) || ball.collide(PongPanel.paddle2)){
                    ball.setDx(-ball.getDx());
                    ball.setXSpeed(ball.getXSpeed() * X_SPEED_FACTOR);
                    ball.setSize(Math.max(MIN_BALL_SIZE, (int) (ball.getWidth() * BALL_SIZE_FACTOR)));
                    ball.setYSpeed((ball.getYSpeed() * Y_SPEED_FACTOR));
                }
            }

            //Powerup collision logic
            if(PongPanel.powerup != null) {
                if (PongPanel.powerup.collide(ball)) {
                    PongPanel.powerup.effectGame();
                }
            }
        }
    }

    public void draw(Graphics g) {
        PongPanel.paddle1.draw(g);
        PongPanel.paddle2.draw(g);
        PongPanel.score.draw(g);

        if(PongPanel.powerup != null)
            PongPanel.powerup.draw(g);

        for(Ball ball : PongPanel.balls){
            if(ball != null)
                ball.draw(g);
        }

        for(Ball fakeball : PongPanel.fakeBalls){
            if(fakeball != null)
                fakeball.draw(g);
        }
        PongPanel.fakeBalls.clear();

    }

    public static void setAIFlag1(boolean AIFlag1) {
        ServeState.AIFlag1 = AIFlag1;
    }

    public static void setAIFlag2(boolean AIFlag2) {
        ServeState.AIFlag2 = AIFlag2;
    }

    public static void setBallSizeFactor(float ballSizeFactor) {
        BALL_SIZE_FACTOR = ballSizeFactor;
    }
}
