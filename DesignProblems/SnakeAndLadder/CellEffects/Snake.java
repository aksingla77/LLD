package DesignProblems.SnakeAndLadder.CellEffects;

import DesignProblems.SnakeAndLadder.Player;

public class Snake extends CellEffect {

    public Snake(int start, int end){
        super(start, end);
    }

    @Override
    public void apply(Player p){
        System.out.println("Player hit a snake! Moving from " + p.currPos + " to " + end);
        p.setPos(end);;
    }
}
