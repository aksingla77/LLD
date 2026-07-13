package DesignProblems.SnakeAndLadder;

import DesignProblems.SnakeAndLadder.CellEffects.Bomb;
import DesignProblems.SnakeAndLadder.CellEffects.Ladder;
import DesignProblems.SnakeAndLadder.CellEffects.Snake;

public class Main {
    public static void main(String[] args){
        Board board = new Board(100);
        Dice dice = new Dice();
        Player player1 = new Player("Akshit");
        Player player2 = new Player("Tiya");

        // Snakes (go down): start > end
        board.addEffect(new Snake(17, 6));
        board.addEffect(new Snake(54, 34));
        board.addEffect(new Snake(62, 19));
        board.addEffect(new Snake(87, 24));
        board.addEffect(new Snake(98, 79));

        // Ladders (go up): start < end
        board.addEffect(new Ladder(3, 22));
        board.addEffect(new Ladder(11, 40));
        board.addEffect(new Ladder(28, 84));
        board.addEffect(new Ladder(51, 67));

        // Bombs (send back to start): pass only the cell
        board.addEffect(new Bomb(37));
        board.addEffect(new Bomb(73));
        board.addEffect(new Bomb(91));

        Game game = new Game(board, dice);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.play();
    }
}
