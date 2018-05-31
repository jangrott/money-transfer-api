package pl.jangrot.mtransfer.service;

import com.google.common.collect.Lists;
import pl.jangrot.mtransfer.dao.ClientAccountDao;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientAccountServiceImpl implements ClientAccountService {

    private final ClientAccountDao dao;

    @Inject
    private ClientAccountServiceImpl(ClientAccountDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Client> getClients() {
        return dao.getClients();
    }

    @Override
    public Client getClient(UUID clientId) {
        return fetchClient(clientId);
    }

    @Override
    public List<Account> getAccounts(UUID clientId) {
        return Lists.newArrayList(fetchAccounts(clientId));
    }

    @Override
    public Account getAccount(UUID clientId, Long accountId) {
        return fetchAccounts(clientId).stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id %s cannot be found", accountId)));
    }

    private Client fetchClient(UUID clientId) {
        return dao.getClient(clientId).orElseThrow(() ->
                new ClientNotFoundException(String.format("Client with id: %s cannot be found", clientId)));
    }

    private Set<Account> fetchAccounts(UUID clientId) {
        return fetchClient(clientId).getAccounts();
    }
}
