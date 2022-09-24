package net.ebank.bank;

import net.ebank.bank.dto.BankAccountDTO;
import net.ebank.bank.dto.CurrentAccountDTO;
import net.ebank.bank.dto.CustomerDTO;
import net.ebank.bank.dto.SavingAccountDTO;
import net.ebank.bank.exceptions.entities.CustomerNotFoundException;
import net.ebank.bank.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
