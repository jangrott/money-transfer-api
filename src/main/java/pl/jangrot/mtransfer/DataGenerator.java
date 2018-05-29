package pl.jangrot.mtransfer;

import com.google.inject.persist.Transactional;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DataGenerator {

    @Inject
    public DataGenerator(EntityManager em) {
        init(em);
    }

    @Transactional
    void init(EntityManager em) {
        em.persist(createClient("John", "Doe"));
        em.persist(createClient("Jane", "Doe"));
    }

    private Client createClient(String firstName, String lastName) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        return client;
    }
}
