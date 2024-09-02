package com.freebills.usecases;

import com.freebills.domain.Account;
import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.entities.AccountEntity;
import com.freebills.gateways.entities.UserEntity;
import com.freebills.gateways.entities.enums.AccountType;
import com.freebills.gateways.entities.enums.BankType;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.gateways.mapper.AccountGatewayMapper;
import com.freebills.repositories.AccountsRepository;
import com.freebills.repositories.UserRepository;
import com.freebills.utils.TestContainerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class UpdateAccountTest extends TestContainerBase {

    @Autowired
    private AccountGateway accountGateway;

    @Autowired
    private CreateTransaction createTransaction;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountGatewayMapper accountGatewayMapper;

    @Autowired
    private TransactionGateway transactionGateway;

    private static AccountEntity accountEntity;

    @BeforeEach
    void beforeSetup() {
        UserEntity userEntity = userRepository.findById(1L).orElse(null);

        final var acc01 = new AccountEntity();
        acc01.setDescription("Conta Inter");
        acc01.setAccountType(AccountType.CHECKING_ACCOUNT);
        acc01.setBankType(BankType.INTER);
        acc01.setUser(userEntity);
        acc01.setArchived(false);
        acc01.setDashboard(false);

        accountEntity = accountsRepository.save(acc01);
    }

    /**
     * Este teste verifica se, ao atualizar uma conta, as transações vinculadas a essa conta não são excluídas.
     * Primeiro, uma transação é criada e vinculada a uma conta.
     * Em seguida, a conta é atualizada.
     * Finalmente, verificamos se a transação ainda existe após a atualização da conta.
     */
    @DisplayName("Teste de atualização de conta sem exclusão de transações vinculadas")
    @Test
    void update() {
        var transactionEntity = new Transaction();
        transactionEntity.setAmount(new BigDecimal("100.00"));
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription("Test transaction");
        transactionEntity.setBarCode(null);
        transactionEntity.setBankSlip(false);
        transactionEntity.setTransactionType(TransactionType.REVENUE);
        transactionEntity.setTransactionCategory(TransactionCategory.HOUSE);
        transactionEntity.setPaid(true);
        transactionEntity.setAccount(accountGatewayMapper.toDomain(accountEntity));

        var transactionSaved = createTransaction.execute(transactionEntity);

        final AccountEntity accountEntity1 = accountEntity;
        accountEntity1.setArchived(true);

        final Account domain = accountGatewayMapper.toDomain(accountEntity1);

        final Account update = accountGateway.update(domain);

        assertEquals(domain.getArchived(), update.getArchived());

        Transaction transactionAfterUpdate = transactionGateway.findById(transactionSaved.getId());
        assertNotNull(transactionAfterUpdate, "Transaction should still exist after account update");
    }
}