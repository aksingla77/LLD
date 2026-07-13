package DesignProblems.AtmMachine.model;

public enum Denomination {
    HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500);

    private int value;
    Denomination(int value){this.value = value;}
    public int getValue(){return this.value;}
}
