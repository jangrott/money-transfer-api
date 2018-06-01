package pl.jangrot.mtransfer.dao;

import org.assertj.core.util.Lists;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClient;

public class ClientAccountDaoImplIntegrationTest extends AbstractDaoIntegrationTest {

    private ClientAccountDao underTest;

    @BeforeClass
    public static void init() {
        AbstractDaoIntegrationTest.init();
    }

    @AfterClass
    public static void tearDown() {
        AbstractDaoIntegrationTest.tearDown();
    }

    @Before
    public void setUp() {
        deleteClients();
        underTest = new ClientAccountDaoImpl(AbstractDaoIntegrationTest::getEm);
    }

    @Test
    public void returnsEmptyListWhenNoClients() {
        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void returnsAllClients() {
        // given
        Client c1 = createClient("Abc", "Qwe");
        Client c2 = createClient("Yui", "Bnm");
        storeClients(Lists.newArrayList(c1, c2));

        // when
        List<Client> actual = underTest.getClients();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void returnsOptionalWithSingleClient() {
        // given
        Client c1 = createClient("Abc", "Qwe");
        storeClients(Lists.newArrayList(c1));

        // when
        Optional<Client> actual = underTest.getClient(c1.getId());

        // then
        assertThat(actual.get()).isEqualTo(c1);
    }

    @Test
    public void returnsEmptyOptionalWhenClientNotFound() {
        // when
        Optional<Client> actual = underTest.getClient(UUID.randomUUID());

        // then
        assertThat(actual.isPresent()).isFalse();
    }

}