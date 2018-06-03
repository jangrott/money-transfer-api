package pl.jangrot.mtransfer.dao;

import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ClientDaoImpl implements ClientDao {

    private final Provider<EntityManager> em;

    @Inject
    public ClientDaoImpl(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public List<Client> getClients() {
        TypedQuery<Client> query = em.get().createQuery("select c from client c", Client.class);

        return query.getResultList();
    }

    @Override
    public Optional<Client> getClient(UUID id) {
        TypedQuery<Client> query = em.get()
                .createQuery("select c from client c where c.id = :id", Client.class)
                .setParameter("id", id);

        Client client = null;

        try {
            client = query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("No client with id: {}", id);
        }

        return Optional.ofNullable(client);
    }
}
