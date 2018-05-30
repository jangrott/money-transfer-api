package pl.jangrot.mtransfer.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClientWithRandomId;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientServiceImpl underTest;

    @Test
    public void getAll_returnsAllClients() {
        // given
        Client c1 = createClientWithRandomId("Abc", "Qaz");
        Client c2 = createClientWithRandomId("Zxc", "Qwe");
        when(clientDao.getAll()).thenReturn(Lists.newArrayList(c1, c2));

        // when
        List<Client> actual = underTest.getAll();

        // then
        assertThat(actual).containsExactlyInAnyOrder(c1, c2);
    }

    @Test
    public void getAll_returnsEmptyListWhenNoClients() {
        // given
        when(clientDao.getAll()).thenReturn(Lists.newArrayList());

        // when
        List<Client> actual = underTest.getAll();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void getById_returnsSingleClient() {
        // given
        Client c1 = createClientWithRandomId("Abc", "Qaz");
        when(clientDao.getById(c1.getId())).thenReturn(Optional.of(c1));

        // when
        Optional<Client> actual = underTest.getById(c1.getId());

        // then
        assertThat(actual.get()).isEqualTo(c1);
    }
}
