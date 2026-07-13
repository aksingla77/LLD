package DesignProblems.AtmMachine;

import DesignProblems.AtmMachine.model.Account;
import DesignProblems.AtmMachine.model.Atm;
import DesignProblems.AtmMachine.model.AtmInventory;
import DesignProblems.AtmMachine.model.Card;
import DesignProblems.AtmMachine.model.Denomination;
import DesignProblems.AtmMachine.service.BankingService;
import DesignProblems.AtmMachine.state.IdleState;

public class Main {
    public static void main(String[] args) {
        // 1. Build the backend and register a card -> account + pin
        BankingService bankingService = new BankingService();
        Card card = new Card(1234);
        Account account = new Account(1, 5000);
        bankingService.registerCard(card, account, 4321);

        // 2. Load the ATM's cash inventory
        AtmInventory inventory = new AtmInventory();
        inventory.insertNotes(Denomination.FIVE_HUNDRED, 10);
        inventory.insertNotes(Denomination.TWO_HUNDRED, 10);
        inventory.insertNotes(Denomination.HUNDRED, 10);

        // 3. Wire up the ATM and put it in the starting state
        Atm atm = new Atm();
        atm.inventory = inventory;
        atm.bankingService = bankingService;
        atm.setState(new IdleState(atm));

        // 4. Drive a session
        atm.insertCard(card);
        atm.enterPin(4321);
        atm.checkBalance();
        atm.ejectCard();

        atm.insertCard(card);
        atm.enterPin(4311);
        atm.ejectCard();
        atm.withdraw(1300);
        atm.checkBalance();
    }
}
