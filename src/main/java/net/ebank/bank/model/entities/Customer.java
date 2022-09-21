package net.ebank.bank.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   private String name;
   private String email;

    @OneToMany(mappedBy ="customer" )// fetch= FetchType.LAZY  (par defaul)  // charger seulement les customers
            //.EAGER  // charger les clients et tous leurs comptes
    private List<BankAccount> bankAccounts;   //oneto many on pense automatiqueement a une liste  car c plusieurs
}
