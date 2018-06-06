package pl.jangrot.mtransfer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.jangrot.mtransfer.dao.AccountDao;
import pl.jangrot.mtransfer.dto.TransferRequest;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.InsufficientFundsException;
import pl.jangrot.mtransfer.exception.InvalidAmountValueException;
import pl.jangrot.mtransfer.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static pl.jangrot.mtransfer.DataGenerator.AccountBuilder.anAccount;

@RunWith(MockitoJUnitRunner.class)
public class InternalTransferServiceTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private InternalTransferService underTest;

    @Test
    public void transfersMoneyBetweenTwoAccounts() {
        // given
        Account fromAccount = anAccount()
                .withId(0L)
                .withBalance(new BigDecimal("900.00"))
                .build();
        Account toAccount = anAccount()
                .withId(1L)
                .withBalance(new BigDecimal("600.00"))
                .build();

        TransferRequest request = new TransferRequest();
        request.setFromAccount(0L);
        request.setToAccount(1L);
        request.setAmount(200);
        when(accountDao.getAccount(0L)).thenReturn(Optional.of(fromAccount));
        when(accountDao.getAccount(1L)).thenReturn(Optional.of(toAccount));

        // when
        underTest.transfer(request);

        // then
        assertThat(fromAccount.getBalance()).isEqualTo(new BigDecimal("700.00"));
        assertThat(toAccount.getBalance()).isEqualTo(new BigDecimal("800.00"));
    }

    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenFromAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        TransferRequest request = new TransferRequest();
        request.setFromAccount(nonExistingAccountId);

        // when
        underTest.transfer(request);
    }


    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenToAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        TransferRequest request = new TransferRequest();
        request.setToAccount(nonExistingAccountId);

        // when
        underTest.transfer(request);
    }

    @Test(expected = InvalidAmountValueException.class)
    public void throwsExceptionWhenAmountNotHigherThanZero() {
        // given
        TransferRequest request = new TransferRequest();
        request.setToAccount(0L);
        request.setFromAccount(1L);
        request.setAmount(0);
        when(accountDao.getAccount(anyLong())).thenReturn(Optional.of(new Account()));

        // when
        underTest.transfer(request);
    }

    @Test(expected = InsufficientFundsException.class)
    public void throwsExceptionWhenInsufficientFunds() {
        // given
        TransferRequest request = new TransferRequest();
        request.setToAccount(0L);
        request.setFromAccount(1L);
        request.setAmount(1000);
        Account account = anAccount()
                .withBalance(new BigDecimal("900.00"))
                .build();
        when(accountDao.getAccount(anyLong())).thenReturn(Optional.of(account));

        // when
        underTest.transfer(request);
    }

}