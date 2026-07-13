package DesignProblems.AtmMachine.state;
import DesignProblems.AtmMachine.model.Atm;
import DesignProblems.AtmMachine.model.Card;

public class IdleState extends AAtmState {
    public IdleState(Atm atm){ super(atm); }

    @Override
    public void insertCard(Card card){
        System.out.println("Card inserted");
        this.atm.addCard(card);
        if(this.atm.bankingService.validateCard(card)){
            this.atm.setState(new CardInsertedState(this.atm));
        }
        else{
            this.atm.resetCard();
            this.atm.setState(new IdleState(atm));
        }
    }
    
}
