package pl.jangrot.mtransfer.util;

import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class TestDataGeneratorHelper {

    public static class ClientBuilder {

        private UUID id;
        private String firstName;
        private String lastName;
        private List<Account> accounts = new ArrayList();

        private ClientBuilder() {
        }

        public static ClientBuilder aClient() {
            return new ClientBuilder();
        }

        public ClientBuilder withId(UUID id) {
            this.id = id;
            return this;
        }

        public ClientBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ClientBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ClientBuilder withAccounts(Account... accounts) {
            this.accounts.addAll(Arrays.asList(accounts));
            return this;
        }

        public Client build() {
            Client client = new Client();

            client.setId(id);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            accounts.forEach(client::addAccount);

            return client;
        }
    }

    public static class AccountBuilder {

        private Long id;
        private BigDecimal balance;

        private AccountBuilder() {
        }

        public static AccountBuilder anAccount() {
            return new AccountBuilder();
        }

        public AccountBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AccountBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Account build() {
            Account account = new Account();

            account.setId(id);
            account.setBalance(balance);

            return account;
        }

    }
}
