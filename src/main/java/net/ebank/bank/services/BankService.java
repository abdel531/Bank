package net.ebank.bank.services;

import net.ebank.bank.dto.*;
import net.ebank.bank.exceptions.entities.BankAccountNotFoundException;
import net.ebank.bank.exceptions.entities.CustomerNotFoundException;
import net.ebank.bank.exceptions.others.AmounInsufficientException;
import net.ebank.bank.exceptions.others.NullAmountException;
import net.ebank.bank.model.entities.BankAccount;
import net.ebank.bank.model.entities.CurrentAccount;

import net.ebank.bank.model.entities.Customer;
import net.ebank.bank.model.entities.SavingAccount;

import java.util.List;

public interface BankService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long CustomerId) throws CustomerNotFoundException;
    SavingAccount saveSavingAccount(double initialBalance, double interestRate, Long CustomerId) throws CustomerNotFoundException;
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<Customer> ListCustomer();

    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    List<BankAccount> ListBankAccount();
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, NullAmountException, AmounInsufficientException;

    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException, NullAmountException;
    void transfer(String accountIdSource,String accountIdDestination,double amount,String description) throws BankAccountNotFoundException, AmounInsufficientException, NullAmountException;

    BankAccountDTO getBankAccountDTO(String accountId) throws BankAccountNotFoundException;

    CustomerDTO saveOrUpdateCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerDTO(Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomerDTO();

    CurrentAccountDTO saveCurrentAccountDTO(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccountDTO saveSavingAccountDTO(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<BankAccountDTO> listBankAccountDTO();

    List<AccountOperationDTO> historyAccount(String accountId);
}
