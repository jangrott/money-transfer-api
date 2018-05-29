package pl.jangrot.mtransfer.dao;

import pl.jangrot.mtransfer.model.Client;

import java.util.List;

public interface ClientDao {

    List<Client> getAll();
}
