package net.ebank.bank.exceptions.entities;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String message) {

        super(message);
    }
}
