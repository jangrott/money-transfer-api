package pl.jangrot.mtransfer.util;

import org.assertj.core.util.Lists;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class TestDataGenerator {

    // TODO: refactor to builder
    public static Client createClient(String firstName, String lastName) {
        return createClient(null, firstName, lastName, Lists.newArrayList());
    }

    public static Client createClient(UUID id, String firstName, String lastName) {
        return createClient(id, firstName, lastName, Lists.newArrayList());
    }

    public static Client createClientWithAccounts(String firstName, String lastName, List<Account> accounts) {
        return createClient(null, firstName, lastName, accounts);
    }

    public static Client createClientWithAccounts(UUID id, String firstName, String lastName, List<Account> accounts) {
        return createClient(id, firstName, lastName, accounts);
    }

    public static Account createAccount(BigDecimal balance) {
        return createAccount(null, balance);
    }

    public static Account createAccount(Long id, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }

    private static Client createClient(UUID id, String firstName, String lastName, List<Account> accounts) {
        Client client = new Client();
        client.setId(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        accounts.forEach(client::addAccount);
        return client;
    }
}
