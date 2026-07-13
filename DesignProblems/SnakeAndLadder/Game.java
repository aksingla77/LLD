package DesignProblems.SnakeAndLadder;

import java.util.ArrayList;
import java.util.List;

public class Game {
    List<Player> players;
    Board board;
    Dice dice; 
    int currentPlayerIndex = 0;
    boolean gameOver = false;

    public Game(Board board, Dice dice){
        this.board = board;
        this.dice = dice;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void play(){
        while(!gameOver) {
            playTurn();
        }
    }

    public void playTurn(){
        Player player = players.get(currentPlayerIndex);
        int steps = dice.roll();
        board.movePlayer(player, steps);
        System.out.println(player.name + " rolled " + steps + " -> pos " + player.currPos);
        if (player.currPos == board.getWinningPos()) {
            System.out.println(player.name + " wins!");
            gameOver = true;
            return;
        }

        // rotate to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
