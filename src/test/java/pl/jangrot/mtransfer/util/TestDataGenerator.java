package pl.jangrot.mtransfer.util;

import pl.jangrot.mtransfer.model.Client;

import java.util.UUID;

public final class TestDataGenerator {

    public static Client createClient(String firstName, String lastName) {
        return createClient(null, firstName, lastName);
    }

    public static Client createClientWithRandomId(String firstName, String lastName) {
        return createClient(UUID.randomUUID(), firstName, lastName);
    }

    public static Client createClient(UUID id, String firstName, String lastName) {
        Client client = new Client();
        client.setId(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        return client;
    }
}
