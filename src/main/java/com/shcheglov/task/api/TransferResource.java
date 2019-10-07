package com.shcheglov.task.api;

import com.shcheglov.task.model.Transfer;
import com.shcheglov.task.service.AccountService;
import com.shcheglov.task.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Anton
 */
@Path("/api/transfer")
@Produces({APPLICATION_JSON})
public class TransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransferResource.class.getName());

    private final TransferService transferService;
    private final AccountService accountService;

    public TransferResource(final TransferService transferService, final AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    @GET
    public List<Transfer> getAllTransfers() {
        LOG.info(">> getAllTransfers");
        return transferService.getAll();
    }

    @POST
    public Transfer createTransfer(@QueryParam("fromAccount") @NotNull final Long fromAccount,
                                   @QueryParam("toAccount") @NotNull final Long toAccount,
                                   @QueryParam("amount") @NotNull final Long amount,
                                   @QueryParam("description") final String description) {
        LOG.info(String.format(">> createTransfer: fromAccount=%s, toAccount=%s, amount=%s, description=%s",
                fromAccount, toAccount, amount, description));
        final Transfer newTransfer = new Transfer(
                LocalDateTime.now(),
                accountService.getById(fromAccount),
                accountService.getById(toAccount),
                amount,
                description);
        return transferService.save(newTransfer);
    }

    @GET
    @Path("/{transferId}")
    public Transfer getTransferById(@PathParam("transferId") @NotNull final Long transferId) {
        LOG.info(">> getTransferById: " + transferId);
        return transferService.getById(transferId);
    }

}
