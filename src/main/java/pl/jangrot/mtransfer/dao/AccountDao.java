package pl.jangrot.mtransfer.dao;

import pl.jangrot.mtransfer.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountDao {

    List<Account> getAccounts(UUID clientId);

    Optional<Account> getAccount(UUID clientId, Long accountId);
}
