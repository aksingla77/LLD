package DesignProblems.SnakeAndLadder.CellEffects;

import DesignProblems.SnakeAndLadder.Player;

public class Bomb extends CellEffect {

    public Bomb(int start){
        super(start, 0);
    }

    @Override
    public void apply(Player p){
        System.out.println("Player hit a bomb! Moving from " + p.currPos + " to " + end);
        p.setPos(end);;
    }
}
