package net.ebank.bank.services;

import net.ebank.bank.exceptions.entities.BankAccountNotFoundException;
import net.ebank.bank.exceptions.entities.CustmerNotFoundException;
import net.ebank.bank.exceptions.others.AmounInsufficientException;
import net.ebank.bank.exceptions.others.NullAmountException;
import net.ebank.bank.model.entities.BankAccount;
import net.ebank.bank.model.entities.CurrentAccount;

import net.ebank.bank.model.entities.Customer;
import net.ebank.bank.model.entities.SavingAccount;

import java.util.List;

public interface BankService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, String CustomerId) throws CustmerNotFoundException;
    SavingAccount saveSavingAccount(double initialBalance, double interestRate, String CustomerId) throws CustmerNotFoundException;
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<Customer> ListCustomer();
    List<BankAccount> ListBankAccount();
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, NullAmountException, AmounInsufficientException;

    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException, NullAmountException;
    void transfer(String accountIdSource,String accountIdDestination,double amount,String description) throws BankAccountNotFoundException, AmounInsufficientException, NullAmountException;

}
