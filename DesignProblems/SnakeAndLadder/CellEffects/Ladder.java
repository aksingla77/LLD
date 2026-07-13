package DesignProblems.SnakeAndLadder.CellEffects;

import DesignProblems.SnakeAndLadder.Player;

public class Ladder extends CellEffect {

    public Ladder(int start, int end){
        super(start, end);
    }

    @Override
    public void apply(Player p){
        System.out.println("Player hit a ladder! Moving from " + p.currPos + " to " + end);
        p.setPos(end);;
    }
}
