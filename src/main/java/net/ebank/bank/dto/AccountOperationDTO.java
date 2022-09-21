package net.ebank.bank.dto;

import lombok.Data;
import net.ebank.bank.model.enums.OperationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;

}
