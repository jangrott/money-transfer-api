package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    List<Client> getAll();

    Optional<Client> getById(UUID id);
}
