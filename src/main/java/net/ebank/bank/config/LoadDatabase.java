package net.ebank.bank.config;

import net.ebank.bank.dto.BankAccountDTO;
import net.ebank.bank.dto.CurrentAccountDTO;
import net.ebank.bank.dto.CustomerDTO;
import net.ebank.bank.dto.SavingAccountDTO;
import net.ebank.bank.exceptions.entities.CustomerNotFoundException;
import net.ebank.bank.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;
@Configuration
public  class  LoadDatabase {

    @Bean
    CommandLineRunner commandLineRunner(BankService bankAccountService) {
        return args -> {
            Stream.of("marc", "vidal", "justin").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@gmail.com");
                bankAccountService.saveOrUpdateCustomer(customerDTO);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*9000,8500, customer.getId());
                    bankAccountService.saveSavingAccount(Math.random()*12000,8.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            List<BankAccountDTO> bankAccountDTOS = bankAccountService.listBankAccountDTO();
            for (BankAccountDTO bankAccountDTO: bankAccountDTOS){
                String accountId;
                if (bankAccountDTO instanceof SavingAccountDTO){
                    accountId = ((SavingAccountDTO) bankAccountDTO).getId();
                }else {
                    accountId = ((CurrentAccountDTO) bankAccountDTO).getId();
                }
                bankAccountService.credit(accountId, 10000 + Math.random()*12000,"credit");
                bankAccountService.debit(accountId, 1000 + Math.random()*9000,"debit");
            }
        };
    }
}
