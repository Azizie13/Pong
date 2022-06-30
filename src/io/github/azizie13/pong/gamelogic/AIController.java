package io.github.azizie13.pong.gamelogic;

import io.github.azizie13.pong.entities.Ball;
import io.github.azizie13.pong.entities.Paddle;
import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static io.github.azizie13.pong.gui.PongPanel.*;

public class AIController {

    private final Paddle paddle;
    private final Ball ball;
    private int randVal;
    private boolean randomFlag, perfectFlag;

    public static Map<Integer, Integer> difficulty;
    static {
        difficulty = new HashMap<>();
        difficulty.put(8, 10);
        difficulty.put(7, 30);
        difficulty.put(6, 60);
        difficulty.put(5, 70);
        difficulty.put(4, 80);
        difficulty.put(3, 90);
        difficulty.put(2, 100);
        difficulty.put(1, 150);
        difficulty.put(0, 200);
        difficulty.put(-1, 210);
        difficulty.put(-2, 230);
        difficulty.put(-3, 240);
        difficulty.put(-4, 245);
        difficulty.put(-5, 250);
        difficulty.put(-6, 300);
        difficulty.put(-7, 320);
        difficulty.put(-8, 400);
    }

    public AIController(Paddle paddle, Ball ball){
        this.paddle = paddle;
        this.ball = ball;
        this.randomFlag = true;
        this.perfectFlag = false;
    }


    public void processAI(){

        if(this.ball.getDx() > 0 && this.paddle.id == 2){
            this.rightPaddleAi();
            randomFlag = true;
        }else if(ball.getDx() < 0 && paddle.id == 1){
            this.leftPaddleAi();
            randomFlag = true;
        }else if(randomFlag){
            randomFlag = false;
            int scoreDiff = PongPanel.score.getScore(1) - PongPanel.score.getScore(2);

            if(scoreDiff > 0){scoreDiff = Math.min(8, scoreDiff);}
            if(scoreDiff < 0){scoreDiff = Math.max(-8, scoreDiff);}

            int minErr = -1 * difficulty.get(scoreDiff);
            int maxErr = difficulty.get(scoreDiff) * 2;
            randVal = PongPanel.random.nextInt(maxErr) + minErr;

        }
    }

    private Ball predictBall(){
        Ball predictedBall = this.ball;
        int futureX = predictedBall.x, futureY = predictedBall.y;
        float futureDy = predictedBall.getDy();

        while(true){
            if(futureY <= 0){
                futureDy *= -1;
            } else if(futureY >= GAME_HEIGHT - predictedBall.height){
                futureDy *= -1;
            }

            Ball rayCast = new Ball(new Rectangle( futureX,  futureY, 2, 2), Color.GREEN);
            fakeBalls.add(rayCast);
            if((futureX >= this.paddle.x && this.paddle.id == 2) || (futureX <= this.paddle.x && this.paddle.id == 1)){
                predictedBall = new Ball(rayCast);
                break;
            }


            futureX += predictedBall.getDx() * predictedBall.getXSpeed();
            futureY += futureDy * predictedBall.getYSpeed();
        }
        return predictedBall;
    }

    private void leftPaddleAi() {
        Ball predictedBall = this.predictBall();

        if(this.ball.x <= PADDLE_WIDTH*10 && !this.perfectFlag){
            predictedBall.setLocation(predictedBall.x, predictedBall.y + randVal);
        }

        this.movePaddle(predictedBall);

        if(predictedBall.y >= this.paddle.y + 5 && predictedBall.y <= this.paddle.y + this.paddle.height/2 - 5){
            this.paddle.stopMoving();
        }
    }

    private void rightPaddleAi() {
        Ball predictedBall = this.predictBall();

        if(this.ball.x >= GAME_WIDTH - PADDLE_WIDTH*10 && !this.perfectFlag){
            predictedBall.setLocation(predictedBall.x, predictedBall.y + randVal);
        }

        this.movePaddle(predictedBall);

        if(predictedBall.y >= this.paddle.y + 10 && predictedBall.y <= this.paddle.y + this.paddle.height/2 - 10){
            this.paddle.stopMoving();
        }
    }

    private void movePaddle(Ball predictedBall){
        if(predictedBall.y < this.paddle.y){
            this.paddle.moveUp();
        }

        if(predictedBall.y > this.paddle.y + this.paddle.height/2){
            this.paddle.moveDown();
        }
    }

    private void classicAI(){
//        Use this to play using simple AI

        if(ball.getDx() < 0){
            paddle.stopMoving();
            return;
        }

        if(ball.getDx() > 0){
            if(ball.y < paddle.y){
                paddle.moveUp();
                return;
            }

            if(ball.y > paddle.y + paddle.height){
                paddle.moveDown();
            }
        }
    }

    public void setPerfectFlag(boolean flag){
        this.perfectFlag = flag;
    }
}
