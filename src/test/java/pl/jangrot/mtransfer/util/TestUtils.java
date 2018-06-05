package pl.jangrot.mtransfer.util;

import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static Client getClientByFirstAndLastName(List<Client> clients, String firstName, String lastName) {
        return clients.stream()
                .filter(client -> firstName.equals(client.getFirstName()) && lastName.equals(client.getLastName()))
                .findFirst()
                .orElse(null);
    }

    public static UUID extractClientId(Client client) {
        return client.getId();
    }

    public static Account extractAccount(Client client) {
        return client.getAccounts().stream().findFirst().orElse(null);
    }

    public static BigDecimal toBigDecimal(String balance) {
        return new BigDecimal(balance);
    }
}
