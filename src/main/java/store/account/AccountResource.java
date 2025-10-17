package store.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AccountResource implements AccountController {

    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<AccountOut> create(AccountIn in) {
        // parser AccountIn to Account
        Account account = AccountParser.to(in);

        Account saved = accountService.create(account);

        // parser Account to AccountOut and build to
        // HATEAOS standard
        return ResponseEntity
            .created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saved.id())
                    .toUri()
            ).body(AccountParser.to(saved));
    }

    @Override
    public ResponseEntity<AccountOut> findById(String id) {
        return ResponseEntity
            .ok(AccountParser.to(accountService.findById(id)));
    }

    @Override
    public ResponseEntity<AccountOut> findByEmailAndPassword(AccountIn in) {
        return ResponseEntity
            .ok()
            .body(AccountParser.to(
                accountService.findByEmailAndPassword(in.email(), in.password())
            ));
    }

    @Override
    public ResponseEntity<List<AccountOut>> findAll() {
        return ResponseEntity
            .ok()
            .body(AccountParser.to(accountService.findAll()));
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return ResponseEntity
            .noContent()
            .build();
    }

    @Override
    public ResponseEntity<AccountOut> whoAmI(String idAccount) {
        final Account found = accountService.findById(idAccount);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(AccountParser.to(found));
    }
    
}
