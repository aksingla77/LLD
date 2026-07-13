package DesignProblems.AtmMachine.model;

import DesignProblems.AtmMachine.service.BankingService;
import DesignProblems.AtmMachine.state.IAtmState;

public class Atm {
    public Card currentCard;
    public AtmInventory inventory;
    public IAtmState currentState;
    public BankingService bankingService;

    public void setState(IAtmState state){
        this.currentState = state;
    }

    public void insertCard(Card card){
        currentState.insertCard(card);
    }
    public void ejectCard(){
        currentState.ejectCard();
    }
    public void enterPin(int Pin){
        currentState.enterPin(Pin);
    }
    public void withdraw(int amount){
        currentState.withdraw(amount);
    }
    public void deposit(int amount){
        currentState.deposit(amount);
    }
    public void checkBalance(){
        currentState.checkBalance();
    }

    public void addCard(Card card){
        currentCard = card;
    }

    public void resetCard(){
        this.currentCard = null;
    }
}
