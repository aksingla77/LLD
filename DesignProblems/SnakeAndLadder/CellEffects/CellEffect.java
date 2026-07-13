package DesignProblems.SnakeAndLadder.CellEffects;

import DesignProblems.SnakeAndLadder.Player;

public abstract class CellEffect {
    public int start;
    public int end;
    public CellEffect(int start, int end){
        this.start = start;
        this.end = end;
    }
    public void apply(Player p){};
}
