package DesignProblems.AtmMachine.state;

import java.util.Map;

import DesignProblems.AtmMachine.model.Atm;
import DesignProblems.AtmMachine.model.Denomination;

public class PinEnteredState extends AAtmState {
    public PinEnteredState(Atm atm){
        super(atm);
    }

    @Override
    public void checkBalance(){
        try{
            int balance = atm.bankingService.getBalance(atm.currentCard);
            System.out.println("Current balance: " + balance);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void withdraw(int amount){
        if(!atm.inventory.canDispense(amount)){
            System.out.println("ATM cannot dispense " + amount + " with the available notes");
            return;
        }
        try{
            atm.bankingService.withdraw(atm.currentCard, amount);
            Map<Denomination, Integer> dispensed = atm.inventory.dispenseNotes(amount);
            System.out.println("Dispensed: " + dispensed);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deposit(int amount){
        try{
            atm.bankingService.deposit(atm.currentCard, amount);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void ejectCard(){
        System.out.println("Card ejected");
        atm.resetCard();
        atm.setState(new IdleState(atm));
    }
}
