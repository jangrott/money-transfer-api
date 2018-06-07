package pl.jangrot.mtransfer.service;

import com.google.common.collect.Lists;
import org.junit.*;
import pl.jangrot.mtransfer.dao.AbstractDaoIntegrationTest;
import pl.jangrot.mtransfer.dao.AccountDaoImpl;
import pl.jangrot.mtransfer.dto.TransferRequest;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.jangrot.mtransfer.DataGenerator.AccountBuilder.anAccount;
import static pl.jangrot.mtransfer.DataGenerator.ClientBuilder.aClient;

public class InternalTransferServiceConcurrencyTest extends AbstractDaoIntegrationTest {

    private static final int NUM_OF_THREADS = 1000;

    private AccountDaoImpl accountDao;
    private TransferService underTest;

    @BeforeClass
    public static void init() {
        AbstractDaoIntegrationTest.init();
    }

    @Before
    public void setUp() {
        accountDao = new AccountDaoImpl(AbstractDaoIntegrationTest::getEm);
        underTest = new InternalTransferService(accountDao);
    }

    @After
    public void clearDB() {
        cleanDB();
    }

    @AfterClass
    public static void tearDown() {
        AbstractDaoIntegrationTest.tearDown();
    }

    @Test
    public void ensuresThreadSafetyForConcurrentTransfers() throws InterruptedException {
        Account fromAccount = anAccount()
                .withBalance(BigDecimal.valueOf(1000000.55))
                .build();
        Account toAccount = anAccount()
                .withBalance(BigDecimal.valueOf(500000.55))
                .build();
        Client client = aClient()
                .withFirstName("John")
                .withLastName("Doe")
                .withAccounts(fromAccount, toAccount)
                .build();
        storeClients(Lists.newArrayList(client));

        Collection<Callable<Boolean>> tasks = new ArrayList<>(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            tasks.add(() -> underTest.transfer(createTransferRequest(fromAccount, toAccount, 1.00F)));
            tasks.add(() -> underTest.transfer(createTransferRequest(toAccount, fromAccount, 2.00F)));
        }

        Executors.newFixedThreadPool(4).invokeAll(tasks);

        assertThat(fromAccount.getBalance()).isEqualTo(BigDecimal.valueOf(1001000.55));
        assertThat(toAccount.getBalance()).isEqualTo(BigDecimal.valueOf(499000.55));
        assertThat(accountDao.getAccount(fromAccount.getId()).get().getBalance()).isEqualTo(BigDecimal.valueOf(1001000.55));
        assertThat(accountDao.getAccount(toAccount.getId()).get().getBalance()).isEqualTo(BigDecimal.valueOf(499000.55));
    }

    private TransferRequest createTransferRequest(Account fromAccount, Account toAccount, float amount) {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccount(fromAccount.getId());
        transferRequest.setToAccount(toAccount.getId());
        transferRequest.setAmount(amount);
        return transferRequest;
    }
}