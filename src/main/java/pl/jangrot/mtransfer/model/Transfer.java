package pl.jangrot.mtransfer.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transfer {

    private Account toAccount;

    private Account fromAccount;

    private BigDecimal amount;
}
