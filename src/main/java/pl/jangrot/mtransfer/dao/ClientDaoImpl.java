package pl.jangrot.mtransfer.dao;

import com.google.inject.Provider;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

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
}
