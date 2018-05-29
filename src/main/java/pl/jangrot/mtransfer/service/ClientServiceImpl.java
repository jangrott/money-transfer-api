package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientDao dao;

    @Inject
    private ClientServiceImpl(ClientDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Client> getAll() {
        return dao.getAll();
    }
}
