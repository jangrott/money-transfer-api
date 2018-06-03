package pl.jangrot.mtransfer;

import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Set;

public class DataGenerator {

    @Inject
    public DataGenerator(EntityManager em) {
        init(em);
    }

    @Transactional
    void init(EntityManager em) {
        em.persist(
                createClient("Mont", "Blanc", Sets.newHashSet(createAccount(BigDecimal.TEN)))
        );
        em.persist(
                createClient("John", "Doe", Sets.newHashSet(createAccount(BigDecimal.valueOf(10000))))
        );
        em.persist(
                createClient("Jane", "Doe", Sets.newHashSet(
                        createAccount(BigDecimal.ZERO), createAccount(BigDecimal.valueOf(100000))))
        );
    }

    private Client createClient(String firstName, String lastName, Set<Account> accounts) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        accounts.forEach(client::addAccount);
        return client;
    }

    private Account createAccount(BigDecimal balance) {
        Account account = new Account();
        account.setBalance(balance);
        return account;
    }
}
