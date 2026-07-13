package DesignProblems.SnakeAndLadder;

public class Player {
    public String name;
    public int currPos;

    public Player(String name){
        this.name = name;
        currPos = 0;
    }

    public void setPos(int newPos){
        this.currPos = newPos;
    }
}
