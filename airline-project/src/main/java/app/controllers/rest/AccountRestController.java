package app.controllers.rest;

import app.dto.account.AccountDTO;
import app.entities.account.Account;
import app.entities.account.Role;
import app.services.interfaces.RoleService;
import app.services.interfaces.AccountService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MethodNotSupportedException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Account REST")
@Tag(name = "Account REST", description = "API для операций с пользователем(Account)")
@RestController
@RequestMapping("/api/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;
    private final RoleService roleService;


    @GetMapping
    @ApiOperation(value = "Get list of all Accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account found"),
            @ApiResponse(code = 204, message = "account not found")
    })
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@PageableDefault(sort = {"id"})  Pageable pageable) {
        log.info("getAllUsers: get all users");
        var users = accountService.getAllAccounts(pageable);

        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(
                        users.stream()
                                .map(AccountDTO::new)
                                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account found"),
            @ApiResponse(code = 404, message = "account not found")
    })
    public ResponseEntity<AccountDTO> getAccountById(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable Long id) {
        log.info("getUserById: get user by id. id = {}", id);
        var account = accountService.getAccountById(id);
        return account.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new AccountDTO(account.get()), HttpStatus.OK);
    }

    @GetMapping("/auth")
    @ApiOperation(value = "Get authenticated Account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account found")
    })
    public ResponseEntity<AccountDTO> getAuthenticatedAccount() {
        log.info("getAuthenticatedUser: get authenticated user");
        Account authAccount = accountService.getAccountByEmail(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
        );
        return ResponseEntity.ok(new AccountDTO(authAccount));
    }

    @PostMapping
    @ApiOperation(value = "Create Account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account added")
    })
    public ResponseEntity<AccountDTO> addAccount(
            @ApiParam(
                    name = "account",
                    value = "Account model"
            )
            @RequestBody @Valid AccountDTO accountDTO) throws MethodNotSupportedException {
        log.info("addUser: add new user with email={}", accountDTO.getEmail());
        accountService.saveAccount(accountDTO.convertToEntity());
        return ResponseEntity.ok(new AccountDTO(
                accountService.getAccountByEmail(accountDTO.getEmail())
        ));
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Edit existed Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "accountDTO edited")
    })
    public ResponseEntity<AccountDTO> updateAccount(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "accountDTO",
                    value = "Account model"
            )
            @RequestBody AccountDTO accountDTO) throws MethodNotSupportedException {
        log.info("updateUser - update accountDTO with id = {}", id);
        accountService.updateAccount(id, accountDTO.convertToEntity());
        return new ResponseEntity<>(new AccountDTO(accountService.getAccountById(id).get()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account deleted"),
            @ApiResponse(code = 404, message = "account not found")
    })
    public ResponseEntity<Void> deleteAccountById(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable Long id) {
        log.info("deleteUserById: delete user with id = {}", id);
        var user = accountService.getAccountById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all-roles")
    @ApiOperation(value = "returns all roles which saved in db")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return all unique roles which saved in db"),
            @ApiResponse(code = 204, message = "no one role saved in db")
    })
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> allRolesFromDb = roleService.getAllRoles();
        if (allRolesFromDb.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(allRolesFromDb);
    }
}
