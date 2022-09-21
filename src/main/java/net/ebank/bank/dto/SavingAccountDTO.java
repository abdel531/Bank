package net.ebank.bank.dto;

import lombok.Data;
import net.ebank.bank.model.enums.AccountStatus;

import java.util.Date;
@Data
public class SavingAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private double interestRate;
    private CustomerDTO customerDTO;
}
