package app.services;

import app.entities.account.Account;
import app.repositories.AccountRepository;
import app.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleServiceImpl roleService;

    @Override
    public void saveAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        account.setRoles(roleService.saveRolesToUser(account));
        if (account.getAnswerQuestion() != null) {
            account.setAnswerQuestion(encoder.encode(account.getAnswerQuestion()));
        }
        accountRepository.saveAndFlush(account);
    }

    @Override
    public void updateAccount(Long id, Account account) {
        var editUser = accountRepository.getAccountById(id);
        if (!account.getPassword().equals(editUser.getPassword())) {
            editUser.setPassword(encoder.encode(account.getPassword()));
        }
        if (!account.getAnswerQuestion()
                .equals(accountRepository.findById(id).orElse(null).getAnswerQuestion())) {
            editUser.setAnswerQuestion(encoder.encode(account.getAnswerQuestion()));
        }

        editUser.setRoles(roleService.saveRolesToUser(account));
        editUser.setEmail(account.getEmail());
        accountRepository.saveAndFlush(editUser);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }

    @Override
    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }
}
