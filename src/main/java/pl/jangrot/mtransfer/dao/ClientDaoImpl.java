package pl.jangrot.mtransfer.dao;

import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.exception.NonUniqueClientException;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    public List<Client> getAll() {
        List<Client> clients = em.get().createQuery("select c from client c", Client.class).getResultList();
        return clients;
    }

    @Override
    public Optional<Client> getById(UUID id) {
        TypedQuery<Client> query = em.get()
                .createQuery("select c from client c where c.id = :id", Client.class)
                .setParameter("id", id);

        Client client;
        try {
            client = query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("No client with id: {}", id);
            throw new ClientNotFoundException(String.format("Client with id: %s cannot be found", id));
        } catch (NonUniqueResultException e) {
            log.error("More than one client with the same id : {}", id);
            throw new NonUniqueClientException(String.format("More than one client with the same id : %s", id));
        }
        return Optional.ofNullable(client);
    }
}
