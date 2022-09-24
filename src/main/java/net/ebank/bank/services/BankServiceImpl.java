package net.ebank.bank.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ebank.bank.dao.AccountOperationRepo;
import net.ebank.bank.dao.BankAccountRepo;
import net.ebank.bank.dao.CustomerRepo;
import net.ebank.bank.dto.*;
import net.ebank.bank.exceptions.entities.BankAccountNotFoundException;
import net.ebank.bank.exceptions.entities.CustomerNotFoundException;
import net.ebank.bank.exceptions.others.AmounInsufficientException;
import net.ebank.bank.exceptions.others.NullAmountException;
import net.ebank.bank.mappers.DtoMapper;
import net.ebank.bank.model.entities.*;
import net.ebank.bank.model.enums.AccountStatus;
import net.ebank.bank.model.enums.OperationType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j //check this permet de permettre à des log de laisser une trasse sur la console
public class BankServiceImpl implements BankService{
    private DtoMapper dtoMapper; //injection du service
    private CustomerRepo customerRepo;
    private BankAccountRepo bankAccountRepo;
    private AccountOperationRepo accountOperationRepo;
    @Override
    public Customer saveCustomer(Customer customer) {

        log.info("save customer");
        return customerRepo.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        log.info("saving  current account");
        Customer customer =customerRepo.findById(customerId).orElse(null);//on verifi si l'utilisateur exist
        if (customer == null) {
            throw new CustomerNotFoundException("customer not found");
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
    public SavingAccount saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        log.info("save saving account");
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null){
            throw new CustomerNotFoundException("customer not found");
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
    public List<Customer> listCustomer() {
        return customerRepo.findAll();
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, NullAmountException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(amount == 0) {throw new NullAmountException ("null amount");}
        else{

            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setBankAccount(bankAccount);
            accountOperation.setOperationDate(new Date());
            accountOperation.setAmount(amount);
            accountOperation.setDescription(description);
            accountOperation.setType(OperationType.CREDIT);
            accountOperationRepo.save(accountOperation);
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            bankAccountRepo.save(bankAccount);

        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, AmounInsufficientException {

        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance()<amount) {throw new AmounInsufficientException("insuffient amount");}
        else{

            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setBankAccount(bankAccount);
            accountOperation.setOperationDate(new Date());
            accountOperation.setAmount(amount);
            accountOperation.setDescription(description);
            accountOperation.setType(OperationType.DEBIT);
            accountOperationRepo.save(accountOperation);
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            bankAccountRepo.save(bankAccount);

        }
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount, String description) throws BankAccountNotFoundException, AmounInsufficientException, NullAmountException {

        debit (accountIdSource,amount,description);
        credit(accountIdDestination,amount,description);

    }
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        customerRepo.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        customerRepo.deleteById(customerId);
    }
    @Override
    public List<BankAccount> ListBankAccount() {
        return bankAccountRepo.findAll();
    }

    //Mappers   //refactoring

    @Override
    public BankAccountDTO getBankAccountDTO(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);

        if (bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
    }

    @Override
    public CustomerDTO saveOrUpdateCustomer(CustomerDTO customerDTO){
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        CustomerDTO customerDTO1 = dtoMapper.fromCustomer(savedCustomer);

        return customerDTO1;
    }

/*    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO){
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        CustomerDTO customerDTO1 = dtoMapper.fromCustomer(savedCustomer);

        return customerDTO1;
    }*/

    @Override
    public CustomerDTO getCustomerDTO(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("customer not found for id ::" +customerId));
        CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
        return customerDTO;
    }


    @Override
    public List<CustomerDTO> listCustomerDTO(){
        List<Customer> customers = customerRepo.findAll();
        //convert customer to customerDTO
        List<CustomerDTO> customerDTOS = customers
                .stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }


    @Override
    public CurrentAccountDTO saveCurrentAccountDTO(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        log.info("saving current account");
        Customer customer = customerRepo.findById(customerId).orElse(null);

        if (customer == null){
            throw new CustomerNotFoundException("customer not found for this id ::"+customerId);
        } else {
            CurrentAccount currentAccount = new CurrentAccount();

            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setCreatedAt(new Date());
            currentAccount.setBalance(initialBalance);
            currentAccount.setOverDraft(overDraft);
            currentAccount.setCustomer(customer);
            CurrentAccount savedCurrentAccount = bankAccountRepo.save(currentAccount);
            return dtoMapper.fromCurrentAccount(savedCurrentAccount);
        }
    }

    @Override
    public SavingAccountDTO saveSavingAccountDTO(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        log.info("saved saving account");
        Customer customer = customerRepo.findById(customerId).orElse(null);

        if (customer == null){
            throw new CustomerNotFoundException("customer not found for this id ::"+customerId);
        } else {
            SavingAccount savingAccount = new SavingAccount();

            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setCreatedAt(new Date());
            savingAccount.setBalance(initialBalance);
            savingAccount.setInterestRate(interestRate);
            savingAccount.setCustomer(customer);
            SavingAccount savedSavingAccount = bankAccountRepo.save(savingAccount);
            return dtoMapper.fromSavingAccount(savedSavingAccount);
        }
    }

    @Override
    public List<BankAccountDTO> listBankAccountDTO(){
        List<BankAccount> bankAccounts = bankAccountRepo.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            }else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    @Override
    public List<AccountOperationDTO> historyAccount(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOS = accountOperations
                .stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        return accountOperationDTOS;
    }

}
