package net.ebank.bank.model.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
