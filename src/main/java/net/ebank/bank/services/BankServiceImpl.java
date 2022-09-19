package net.ebank.bank.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ebank.bank.dao.AccountOperationRepo;
import net.ebank.bank.dao.BankAccountRepo;
import net.ebank.bank.dao.CustomerRepo;
import net.ebank.bank.exceptions.entities.BankAccountNotFoundException;
import net.ebank.bank.exceptions.entities.CustmerNotFoundException;
import net.ebank.bank.model.entities.BankAccount;
import net.ebank.bank.model.entities.CurrentAccount;
import net.ebank.bank.model.entities.Customer;
import net.ebank.bank.model.entities.SavingAccount;
import net.ebank.bank.model.enums.AccountStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j //check this permet de permettre à des log de laisser une trasse sur la console
public class BankServiceImpl implements BankService{

    private CustomerRepo customerRepo;
    private BankAccountRepo bankAccountRepo;
    private AccountOperationRepo accountOperationRepo;
    @Override
    public Customer saveCustomer(Customer customer) {

        log.info("save customer");
        return customerRepo.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, String customerId) throws CustmerNotFoundException {
        log.info("saving  current account");
        Customer customer =customerRepo.findById(customerId).orElse(null);//on verifi si l'utilisateur exist
        if (customer == null) {
            throw new CustmerNotFoundException("customer not found");
        }
        else {
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString()); // allocation de mémoire 32 bit par defaut
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setCreatedAt(new Date());
            currentAccount.setOverDraft(overDraft);
            currentAccount.setBalance(initialBalance);
            return bankAccountRepo.save(currentAccount);

        }

    }

    @Override
    public SavingAccount saveSavingAccount(double initialBalance, double interestRate, String customerId) throws CustmerNotFoundException {
        log.info("save saving account");
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null){
            throw new CustmerNotFoundException("customer not found");
        } else {

            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setCreatedAt(new Date());
            savingAccount.setBalance(initialBalance);
            savingAccount.setInterestRate(interestRate);
            return bankAccountRepo.save(savingAccount);
        }
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("bank account not found"));
        //if bankAccount founded return bankAccount object else return bankAccountNotFoundException
        return bankAccount;
    }

    @Override
    public List<Customer> ListCustomer() {
        return customerRepo.findAll();
    }

    @Override
    public void debit(String accountId, double amount, String description) {

    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount, String description) {

    }
}