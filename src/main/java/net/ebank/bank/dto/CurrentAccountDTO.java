package net.ebank.bank.dto;

import lombok.Data;
import net.ebank.bank.model.enums.AccountStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private double overDraft;
    private CustomerDTO customerDTO;

}
