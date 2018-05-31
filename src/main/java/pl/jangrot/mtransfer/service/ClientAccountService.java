package pl.jangrot.mtransfer.service;

import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientAccountService {

    List<Client> getClients();

    Client getClient(UUID clientId);

    List<Account> getAccounts(UUID clientId);

    Account getAccount(UUID clientId, Long accountId);

}
