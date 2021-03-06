package pl.jangrot.mtransfer.dao;

import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientDao {

    List<Client> getClients();

    Optional<Client> getClient(UUID clientId);

}
