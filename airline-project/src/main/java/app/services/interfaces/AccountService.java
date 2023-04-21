package app.services.interfaces;

import app.entities.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountService {
    void saveAccount(Account user);

    void updateAccount(Long id, Account user);

    Page<Account> getAllAccounts(Pageable pageable);

    Optional<Account> getAccountById(Long id);

    Account getAccountByEmail(String email);

    void deleteAccountById(Long id);

}
