package pl.jangrot.mtransfer.service;

import com.google.inject.Inject;
import pl.jangrot.mtransfer.dto.TransferRequest;
import pl.jangrot.mtransfer.model.Transfer;

import java.util.function.Function;

public class InternalTransferService implements TransferService {

    @Inject
    public InternalTransferService() {

    }

    @Override
    public void transfer(TransferRequest transferRequest) {

    }

    private Function<TransferRequest, Transfer> toTransfer = (transferRequest -> {
        Transfer transfer = new Transfer();
        return transfer;
    });
}
