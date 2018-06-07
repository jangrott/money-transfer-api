package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.dto.TransferRequest;

public interface TransferService {

    boolean transfer(TransferRequest transferRequest);
}
