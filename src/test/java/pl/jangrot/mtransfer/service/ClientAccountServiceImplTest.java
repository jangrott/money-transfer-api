package pl.jangrot.mtransfer.service;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.jangrot.mtransfer.dao.ClientAccountDao;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.jangrot.mtransfer.util.TestDataGenerator.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientAccountServiceImplTest {

    @Mock
    private ClientAccountDao clientAccountDao;

    @InjectMocks
    private ClientAccountServiceImpl underTest;

    @Test
    public void returnsAllClients() {
        // given
        Client c1 = createClient(UUID.randomUUID(), "Abc", "Qaz");
        Client c2 = createClient(UUID.randomUUID(), "Zxc", "Qwe");
        when(clientAccountDao.getClients()).thenReturn(Lists.newArrayList(c1, c2));

        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void returnsEmptyListWhenNoClients() {
        // given
        when(clientAccountDao.getClients()).thenReturn(Lists.newArrayList());

        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsSingleClient() {
        // given
        Client c1 = createClient(UUID.randomUUID(), "Abc", "Qaz");
        when(clientAccountDao.getClient(c1.getId())).thenReturn(Optional.of(c1));

        // when
        Client actual = underTest.getClient(c1.getId());

        // then
        assertThat(actual).isEqualTo(c1);
    }

    @Test(expected = ClientNotFoundException.class)
    public void throwsExceptionWhenClientNotFound() {
        // given
        when(clientAccountDao.getClient(any(UUID.class))).thenReturn(Optional.empty());

        // when
        underTest.getClient(UUID.randomUUID());
    }

    @Test
    public void returnsEmptyAccountListWhenNoAccountsForGivenClient() {
        // given
        UUID clientId = UUID.randomUUID();
        when(clientAccountDao.getClient(eq(clientId))).thenReturn(Optional.of(createClient(clientId, "Abc", "Zxc")));

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
        Set<Account> accounts = ImmutableSet.of(a1, a2);
        when(clientAccountDao.getClient(clientId))
                .thenReturn(Optional.of(createClientWithAccounts(clientId, "Abc", "Qaz", accounts)));

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
        Set<Account> accounts = ImmutableSet.of(a);
        when(clientAccountDao.getClient(clientId))
                .thenReturn(Optional.of(createClientWithAccounts(clientId, "Abc", "Qaz", accounts)));

        // when
        Account actual = underTest.getAccount(clientId, accountId);

        // then
        assertThat(actual).isEqualTo(a);
    }

    @Test(expected = AccountNotFoundException.class)
    public void throwsExceptionWhenAccountNotFoundForGivenUser() {
        // given
        UUID clientId = UUID.randomUUID();
        when(clientAccountDao.getClient(any(UUID.class))).thenReturn(Optional.of(createClient(clientId, "Avc", "Qwa")));

        // when
        underTest.getAccount(clientId, 1L);
    }
}
