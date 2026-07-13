package DesignProblems.AtmMachine.model;

public class Account {
    public int accountNumber;
    public int balance;

    public Account(int accountNumber, int balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public int getBalance() { return this.balance; }

    public void deposit(int amount){
        this.balance += amount;
    }

    public void withdraw(int amount) throws Exception {
        if(amount > this.balance) throw new Exception("Insufficient balance");
        this.balance -= amount;
    }
    
}
