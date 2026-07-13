package DesignProblems.AtmMachine.service;
import java.util.HashMap;

import DesignProblems.AtmMachine.model.Account;
import DesignProblems.AtmMachine.model.Card;

public class BankingService {
    private HashMap<Card, Account> cardAccountMap;
    private HashMap<Card, Integer> cardPinMap;

    public BankingService(){
        this.cardAccountMap = new HashMap<>();
        this.cardPinMap = new HashMap<>();
    }

    public void registerCard(Card card, Account account, int pin){
        cardAccountMap.put(card, account);
        cardPinMap.put(card, pin);
    }

    private Account getAccount(Card card){
        return cardAccountMap.get(card);
    }

    public boolean validateCard(Card card){
        return cardAccountMap.containsKey(card);
    }

    public boolean validatePin(Card card, int pin){
        return cardPinMap.get(card) == pin;
    }

    public void deposit(Card card, int amount){
        getAccount(card).deposit(amount);
    }

    public void withdraw(Card card, int amount) throws Exception {
        getAccount(card).withdraw(amount);
    }

    public int getBalance(Card card){
        return getAccount(card).getBalance();
    }
}
