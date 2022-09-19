package net.ebank.bank.dao;

import net.ebank.bank.model.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount,String> {

}
