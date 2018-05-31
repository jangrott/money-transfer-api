package pl.jangrot.mtransfer.util;

import org.assertj.core.util.Sets;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public final class TestDataGenerator {

    // TODO: refactor to builder
    public static Client createClient(String firstName, String lastName) {
        return createClient(null, firstName, lastName, Sets.newHashSet());
    }

    public static Client createClient(UUID id, String firstName, String lastName) {
        return createClient(id, firstName, lastName, Sets.newHashSet());
    }

    public static Client createClientWithAccounts(UUID id, String firstName, String lastName, Set<Account> accounts) {
        return createClient(id, firstName, lastName, accounts);
    }

    public static Account createAccount(Long id, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }

    private static Client createClient(UUID id, String firstName, String lastName, Set<Account> accounts) {
        Client client = new Client();
        client.setId(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setAccounts(accounts);
        return client;
    }
}
