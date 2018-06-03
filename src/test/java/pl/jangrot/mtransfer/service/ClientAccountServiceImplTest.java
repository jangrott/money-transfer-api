package pl.jangrot.mtransfer.service;

import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.jangrot.mtransfer.dao.AccountDao;
import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createAccount;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClient;

@RunWith(MockitoJUnitRunner.class)
public class ClientAccountServiceImplTest {

    @Mock
    private AccountDao accountDao;

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientAccountServiceImpl underTest;

    @Test
    public void returnsAllClients() {
        // given
        Client c1 = createClient(UUID.randomUUID(), "Abc", "Qaz");
        Client c2 = createClient(UUID.randomUUID(), "Zxc", "Qwe");
        when(clientDao.getClients()).thenReturn(Lists.newArrayList(c1, c2));

        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void returnsEmptyListWhenNoClients() {
        // given
        when(clientDao.getClients()).thenReturn(Lists.newArrayList());

        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsSingleClient() {
        // given
        Client c1 = createClient(UUID.randomUUID(), "Abc", "Qaz");
        when(clientDao.getClient(c1.getId())).thenReturn(Optional.of(c1));

        // when
        Client actual = underTest.getClient(c1.getId());

        // then
        assertThat(actual).isEqualTo(c1);
    }

    @Test(expected = ClientNotFoundException.class)
    public void throwsExceptionWhenClientNotFound() {
        // given
        when(clientDao.getClient(any(UUID.class))).thenReturn(Optional.empty());

        // when
        underTest.getClient(UUID.randomUUID());
    }

    @Test
    public void returnsEmptyAccountListWhenNoAccountsForGivenClient() {
        // given
        UUID clientId = UUID.randomUUID();
        when(accountDao.getAccounts(clientId)).thenReturn(Collections.emptyList());

        // when
        List<Account> actual = underTest.getAccounts(clientId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsAllAccountsForGivenClient() {
        // given
        UUID clientId = UUID.randomUUID();
        Account a1 = createAccount(1L, BigDecimal.ZERO);
        Account a2 = createAccount(2L, BigDecimal.TEN);
        List<Account> accounts = ImmutableList.of(a1, a2);
        when(accountDao.getAccounts(clientId)).thenReturn(accounts);

        // when
        List<Account> actual = underTest.getAccounts(clientId);

        // then
        assertThat(actual).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    public void returnsSingleAccountForGivenClient() {
        // given
        UUID clientId = UUID.randomUUID();
        long accountId = 1L;
        Account a = createAccount(accountId, BigDecimal.TEN);
        when(accountDao.getAccount(clientId, 1L)).thenReturn(Optional.of(a));

        // when
        Account actual = underTest.getAccount(clientId, accountId);

        // then
        assertThat(actual).isEqualTo(a);
    }

    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenAccountNotFoundForGivenUser() {
        // given
        long nonExistingAccountId = 1L;
        UUID clientId = UUID.randomUUID();
        when(accountDao.getAccount(clientId, nonExistingAccountId)).thenReturn(Optional.empty());

        // when
        underTest.getAccount(clientId, nonExistingAccountId);
    }
}
