package com.freebills.entities;


import com.freebills.entities.enums.TransactionCategory;
import com.freebills.entities.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotBlank
    @Size(max = 50)
    private String description;

    @Size(max = 50)
    private String barCode;

    private Boolean bankSlip;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionCategory transactionCategory;

    private boolean paid;

    private Long fromAccount;
    private Long toAccount;
    private boolean transactionChange;
    private BigDecimal previousAmount;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionLog> transactionLogs;

    @ManyToOne
    private Account account;

    public Transaction(BigDecimal amount,
                       LocalDate date,
                       String description,
                       TransactionType transactionType,
                       TransactionCategory transactionCategory,
                       boolean paid,
                       Account account) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.paid = paid;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return paid == that.paid &&
                Objects.equals(id, that.id) &&
                amount.compareTo(that.getAmount()) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(barCode, that.barCode) &&
                Objects.equals(bankSlip, that.bankSlip) &&
                transactionType == that.transactionType &&
                transactionCategory == that.transactionCategory &&
                Objects.equals(fromAccount, that.fromAccount) &&
                Objects.equals(toAccount, that.toAccount) &&
                Objects.equals(transactionChange, that.transactionChange) &&
                account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, description, barCode, bankSlip, transactionType, transactionCategory, paid, account);
    }
}
