package DesignProblems.SnakeAndLadder;

import java.util.HashMap;

import DesignProblems.SnakeAndLadder.CellEffects.CellEffect;

public class Board {
    int n;
    HashMap<Integer, CellEffect> cellEffects;

    public Board(int n){
        this.n = n;
        this.cellEffects = new HashMap<>();
    }

    public void addEffect(CellEffect effect){
        cellEffects.put(effect.start, effect);
    }
    
    public void movePlayer(Player p, int steps){
        int newPos = p.currPos + steps;
        if(newPos > n){
            System.out.println("  overshoot: needs exactly " + (n - p.currPos) + ", stays at " + p.currPos);
            return;
        }
        p.setPos(newPos);
        while(cellEffects.containsKey(newPos)){
            cellEffects.get(newPos).apply(p);
            if(newPos == p.currPos) break;
            newPos = p.currPos;
        }
    }

    public int getWinningPos(){
        return n;
    }
}
