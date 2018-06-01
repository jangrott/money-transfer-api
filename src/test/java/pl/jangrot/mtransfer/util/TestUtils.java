package pl.jangrot.mtransfer.util;

import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;

public class TestUtils {

    public static Client getClientByFirstAndLastName(List<Client> clients, String firstName, String lastName) {
        return clients.stream()
                .filter(client -> firstName.equals(client.getFirstName()) && lastName.equals(client.getLastName()))
                .findFirst()
                .orElse(null);
    }

    public static String extractClientId(Client client) {
        return client.getId().toString();
    }

    public static Long extractAccountId(Client client) {
        return client.getAccounts().stream().findFirst().map(Account::getId).orElse(null);
    }

    public static BigDecimal toBigDecimal(String balance) {
        return BigDecimal.valueOf(Float.valueOf(balance));
    }
}
