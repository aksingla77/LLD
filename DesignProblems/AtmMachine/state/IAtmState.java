package DesignProblems.AtmMachine.state;

import DesignProblems.AtmMachine.model.Card;

public interface IAtmState {
    void insertCard(Card card);
    void ejectCard();
    void enterPin(int Pin);
    void withdraw(int amount);
    void deposit(int amount);
    void checkBalance();
}
