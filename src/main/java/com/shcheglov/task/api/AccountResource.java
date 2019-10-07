package com.shcheglov.task.api;

import com.shcheglov.task.model.Account;
import com.shcheglov.task.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Anton
 */
@Path("/api/account")
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class.getName());

    private final AccountService accountService;

    public AccountResource(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces({APPLICATION_JSON})
    public List<Account> getAllAccounts() {
        LOG.info("getAllAccounts");
        return accountService.getAll();
    }

    @POST
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public Account createAccount(@Valid @NotNull final Account newAccount) {
        LOG.info("createAccount: " + newAccount);
        return accountService.save(newAccount);
    }

    @PUT
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public Account updateAccount(@Valid @NotNull final Account updatedAccount) {
        LOG.info("updateAccount: " + updatedAccount);
        return accountService.update(updatedAccount);
    }

    @GET
    @Path("/{accountId}")
    @Produces({APPLICATION_JSON})
    public Account getAccountById(@PathParam("accountId") @NotNull final Long accountId) {
        LOG.info("getAccountById: " + accountId);
        return accountService.getById(accountId);
    }

    @DELETE
    @Path("/{accountId}")
    public void deleteAccount(@PathParam("accountId") @NotNull final Long accountId) {
        LOG.info("deleteAccount: " + accountId);
        accountService.delete(accountId);
    }

}
