package net.ebank.bank.model.entities;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ebank.bank.model.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING) //une seule colonne
public abstract class BankAccount {

    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(value=EnumType.STRING)//.string pour nous donner la valeur au lieu de O,1,
    private AccountStatus status;

    @ManyToOne
    @NotNull
    private Customer customer;
    @OneToMany(mappedBy="bankAccount")
    private List<AccountOperation> accountOperations;

}
