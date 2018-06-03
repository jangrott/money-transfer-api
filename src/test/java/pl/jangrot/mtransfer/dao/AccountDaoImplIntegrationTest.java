package pl.jangrot.mtransfer.dao;

import org.assertj.core.util.Lists;
import org.junit.*;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createAccount;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClientWithAccounts;

public class AccountDaoImplIntegrationTest extends AbstractDaoIntegrationTest {

    private AccountDao underTest;

    @BeforeClass
    public static void init() {
        AbstractDaoIntegrationTest.init();
    }

    @AfterClass
    public static void tearDown() {
        AbstractDaoIntegrationTest.tearDown();
    }

    @Before
    public void setUp() {
        underTest = new AccountDaoImpl(AbstractDaoIntegrationTest::getEm);
    }

    @After
    public void clearDB() {
        cleanDB();
    }

    @Test
    public void returnsAllAccountsForGivenClient() {
        // given
        Account a1 = createAccount(BigDecimal.ZERO);
        Account a2 = createAccount(BigDecimal.TEN);
        List<Account> accounts = Lists.newArrayList(a1, a2);
        Client c = createClientWithAccounts("Abc", "Zxc", accounts);
        storeClients(Lists.newArrayList(c));

        // when
        List<Account> actual = underTest.getAccounts(c.getId());

        // then
        assertThat(actual).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    public void returnsEmptyListWhenNoAccountsForGivenClient() {
        // when
        List<Account> accounts = underTest.getAccounts(UUID.randomUUID());

        // then
        assertThat(accounts).isEmpty();
    }

    @Test
    public void returnsOptionalWithSingleAccountForGivenClient() {
        // given
        Account a = createAccount(BigDecimal.ZERO);
        List<Account> accounts = Lists.newArrayList(a);
        Client c = createClientWithAccounts("Abc", "Zxc", accounts);
        storeClients(Lists.newArrayList(c));

        // when
        Optional<Account> account = underTest.getAccount(c.getId(), a.getId());

        assertThat(account.get()).isEqualTo(a);
    }

    @Test
    public void returnsEmptyOptionalWhenAccountNotFound() {
        // given
        long nonExistingAccountId = 0L;
        UUID clientId = UUID.randomUUID();
        
        // when
        Optional<Account> actual = underTest.getAccount(clientId, nonExistingAccountId);

        // then
        assertThat(actual.isPresent()).isFalse();
    }
}