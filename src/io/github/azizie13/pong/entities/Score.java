package io.github.azizie13.pong.entities;

import io.github.azizie13.pong.gui.PongPanel;

import java.awt.*;

public class Score extends Rectangle{

    private int player1, player2, winner;

    public Score() {
        player1 = 0;
        player2 = 0;
        winner = 0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));

        g.drawLine(PongPanel.GAME_WIDTH/2, 0, PongPanel.GAME_WIDTH/2, PongPanel.GAME_HEIGHT);

        g.drawString(String.valueOf(player1/10) + String.valueOf(player1 % 10), (PongPanel.GAME_WIDTH/2) - 85, 50);
        g.drawString(String.valueOf(player2/10) + String.valueOf(player2 % 10), (PongPanel.GAME_WIDTH/2) + 20, 50);
    }

    public void player1Score() {
        player1++;
        PongPanel.newPaddles();
        PongPanel.newBall();
//        System.out.println("Player 1: " + player1);
    }

    public void player2Score() {
        player2++;
        PongPanel.newPaddles();
        PongPanel.newBall();
//        System.out.println("Player 2: " + player2);
    }

    public int getScore(int playerID){
        if(playerID == 1){return this.player1;}
        if(playerID == 2){return this.player2;}

        return 0;
    }

    public void setWinner(int player){
        this.winner = player;
    }

    public int getWinner() {
        return winner;
    }

    public void resetScore(){
        player1 = 0;
        player2 = 0;
    }

}
