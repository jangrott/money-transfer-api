package pl.jangrot.mtransfer.service;

import com.google.inject.Inject;
import pl.jangrot.mtransfer.dao.AccountDao;
import pl.jangrot.mtransfer.dto.TransferRequest;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.InvalidAmountValueException;
import pl.jangrot.mtransfer.model.Account;

import java.math.BigDecimal;

import static java.lang.String.format;

public class InternalTransferService implements TransferService {

    private final AccountDao accountDao;

    @Inject
    public InternalTransferService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(TransferRequest transferRequest) {
        Account fromAccount = accountDao.getAccount(transferRequest.getFromAccount())
                .orElseThrow(() -> new AccountNotFoundException(
                        format("Account with id %d (fromAccount) cannot be found", transferRequest.getFromAccount())));

        Account toAccount = accountDao.getAccount(transferRequest.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException(
                        format("Account with id %d (toAccount) cannot be found", transferRequest.getToAccount())));

        validateAmount(transferRequest.getAmount());

        BigDecimal amount = BigDecimal.valueOf(transferRequest.getAmount());

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountDao.update(fromAccount);
        accountDao.update(toAccount);
    }

    private void validateAmount(float amount) {
        if (amount <= 0) {
            throw new InvalidAmountValueException("Amount value has to be higher than 0");
        }
    }

}
