package DesignProblems.AtmMachine.state;

import DesignProblems.AtmMachine.model.Atm;

public class CardInsertedState extends AAtmState {
    public CardInsertedState(Atm atm){
        super(atm);
    }

    @Override
    public void enterPin(int pin){
        System.out.println("Pin entered");
        if(this.atm.bankingService.validatePin(atm.currentCard, pin)){
            this.atm.setState(new PinEnteredState(this.atm));
        }
        else{
            System.out.println("Invalid pin - please try again");
        }
    }

    @Override
    public void ejectCard(){
        System.out.println("Card ejected");
        atm.resetCard();
        this.atm.setState(new IdleState(this.atm));
    }
}
