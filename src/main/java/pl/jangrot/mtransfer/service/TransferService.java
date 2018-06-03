package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.dto.TransferRequest;

public interface TransferService {

    void transfer(TransferRequest transferRequest);
}
