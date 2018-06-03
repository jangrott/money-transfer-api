package pl.jangrot.mtransfer.dto;

import lombok.Data;

@Data
public class TransferRequest {

    private Long fromAccount;

    private Long toAccount;

    private float amount;
}
