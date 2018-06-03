package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.dao.AccountDao;
import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class ClientAccountServiceImpl implements ClientAccountService {

    private final AccountDao accountDao;
    private final ClientDao clientDao;

    @Inject
    private ClientAccountServiceImpl(AccountDao accountDao, ClientDao clientDao) {
        this.accountDao = accountDao;
        this.clientDao = clientDao;
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getClients();
    }

    @Override
    public Client getClient(UUID clientId) {
        return clientDao.getClient(clientId).orElseThrow(() ->
                new ClientNotFoundException(String.format("Client with id: %s cannot be found", clientId)));

    }

    @Override
    public List<Account> getAccounts(UUID clientId) {
        return accountDao.getAccounts(clientId);
    }

    @Override
    public Account getAccount(UUID clientId, Long accountId) {
        return accountDao.getAccount(clientId, accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id: %s cannot be found", accountId)));
    }

}
