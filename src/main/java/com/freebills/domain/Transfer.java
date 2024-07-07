package com.freebills.domain;

import com.freebills.gateways.entities.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.freebills.gateways.entities.enums.TransferType.IN;
import static com.freebills.gateways.entities.enums.TransferType.OUT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transfer implements Serializable {

    private Long id;
    private BigDecimal amount;
    private String observation;
    private String description;
    private LocalDate date;
    private TransferType transferType;
    private Account fromAccountId;
    private Account toAccountId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Transfer withTransferTypeIn() {
        return this.toBuilder().transferType(IN).build();
    }

    public Transfer withTransferTypeOut() {
        return this.toBuilder().transferType(OUT).build();
    }
}
