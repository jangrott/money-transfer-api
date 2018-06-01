package pl.jangrot.mtransfer.dao;

import pl.jangrot.mtransfer.model.Client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class AbstractDaoIntegrationTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void init() {
        emf = Persistence.createEntityManagerFactory("mtransfer");
        em = emf.createEntityManager();
    }

    public static void tearDown() {
        em.clear();
        em.close();
        emf.close();
    }

    public static EntityManager getEm() {
        return em;
    }

    protected List<Client> storeClients(List<Client> clients) {
        getEm().getTransaction().begin();
        clients.forEach(getEm()::persist);
        getEm().getTransaction().commit();

        return clients;
    }

    protected void deleteClients() {
        getEm().getTransaction().begin();
        getEm().createQuery("delete from client").executeUpdate();
        getEm().getTransaction().commit();
    }
}
