package DesignProblems.AtmMachine.state;

import DesignProblems.AtmMachine.model.Atm;
import DesignProblems.AtmMachine.model.Card;

public abstract class AAtmState implements IAtmState {
    public Atm atm;

    public AAtmState(Atm atm){
        this.atm = atm;
    }

    private void reject(String message){
        System.out.println(message);
    }

    public void insertCard(Card car){reject("not allowed at this stage");}
    public void ejectCard(){reject("not allowed at this stage");}
    public void enterPin(int Pin){reject("not allowed at this stage");}
    public void withdraw(int amount){reject("not allowed at this stage");}
    public void deposit(int amount){reject("not allowed at this stage");}
    public void checkBalance(){reject("not allowed at this stage");}
}
