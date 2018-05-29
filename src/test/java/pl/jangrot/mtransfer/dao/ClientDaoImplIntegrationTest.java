package pl.jangrot.mtransfer.dao;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClient;

public class ClientDaoImplIntegrationTest extends AbstractDaoIntegrationTest {

    private ClientDao underTest;

    @Before
    public void setUp() {
        deleteClients();
        underTest = new ClientDaoImpl(() -> getEm());
    }

    @Test
    public void getAll_returnsEmptyListWhenNoClients() {
        // given
        // no clients in the database

        // when
        List<Client> actual = underTest.getAll();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void getAll_returnsAllClients() {
        // given
        Client c1 = createClient("Abc", "Qwe");
        Client c2 = createClient("Yui", "Bnm");
        storeClients(Lists.newArrayList(c1, c2));

        // when
        List<Client> actual = underTest.getAll();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    private void deleteClients() {
        getEm().getTransaction().begin();
        getEm().createQuery("delete from client").executeUpdate();
        getEm().getTransaction().commit();
    }


    private void storeClients(List<Client> clients) {
        getEm().getTransaction().begin();
        clients.forEach(getEm()::persist);
        getEm().getTransaction().commit();
    }
}