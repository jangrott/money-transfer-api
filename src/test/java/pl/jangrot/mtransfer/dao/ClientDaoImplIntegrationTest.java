package pl.jangrot.mtransfer.dao;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    public void getById_returnsSingleClient() {
        // given
        Client c1 = createClient("Abc", "Qwe");
        storeClients(Lists.newArrayList(c1));

        // when
        Optional<Client> actual = underTest.getById(c1.getId());

        // then
        assertThat(actual.get()).isEqualTo(c1);
    }

    @Test
    public void getById_throwsExceptionWhenClientNotFound() {
        // given
        UUID id = UUID.randomUUID();
        try {
            // when
            underTest.getById(id);
        } catch (ClientNotFoundException e) {
            // then
            assertThat(e).isInstanceOf(ClientNotFoundException.class)
                    .hasMessage(String.format("Client with id: %s cannot be found", id));
        }
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