package pl.jangrot.mtransfer.rest;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.dto.TransferRequest;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.exception.InsufficientFundsException;
import pl.jangrot.mtransfer.exception.InvalidAmountValueException;
import pl.jangrot.mtransfer.service.ClientAccountService;
import pl.jangrot.mtransfer.service.TransferService;

import javax.inject.Inject;
import java.util.UUID;

import static spark.Spark.*;

@Slf4j
public class RouterImpl implements Router {

    private static final String EMPTY = "";
    private static final String SUCCESSFUL_TRANSFER_BODY = "{\"transferStatus\": \"OK\"}";
    private static final String EXCEPTION_BODY = "{\"statusCode\": %d, \"message\": \"%s\"}";

    private final Gson gson;
    private final ClientAccountService clientAccountService;
    private final TransferService transferService;

    @Inject
    public RouterImpl(Gson gson, ClientAccountService clientAccountService, TransferService transferService) {
        this.gson = gson;
        this.clientAccountService = clientAccountService;
        this.transferService = transferService;
    }

    @Override
    public void start() {
        path("/api", () -> {
            before("/*", (req, res) -> {
                log.info("Received {} request to endpoint: {}", req.requestMethod(), req.uri());
                res.type("application/json");
            });

            path("/clients", () -> {

                get(EMPTY, (req, res) -> gson.toJsonTree(clientAccountService.getClients()));

                path("/:clientId", () -> {

                    get(EMPTY, (req, res) -> {
                        UUID clientId = UUID.fromString(req.params("clientId"));
                        return gson.toJsonTree(clientAccountService.getClient(clientId));
                    });

                    path("/accounts", () -> {

                        get(EMPTY, (req, res) -> {
                            UUID clientId = UUID.fromString(req.params("clientId"));
                            return gson.toJsonTree(clientAccountService.getAccounts(clientId));
                        });

                        get("/:accountId", (req, res) -> {
                            UUID clientId = UUID.fromString(req.params("clientId"));
                            Long accountId = Long.valueOf(req.params("accountId"));
                            return gson.toJsonTree(clientAccountService.getAccount(clientId, accountId));
                        });
                    });
                });
            });

            post("/transfer", (req, res) -> {
                TransferRequest transferRequest = gson.fromJson(req.body(), TransferRequest.class);
                transferService.transfer(transferRequest);
                return SUCCESSFUL_TRANSFER_BODY;
            });

            exception(ClientNotFoundException.class, (ex, req, res) -> {
                res.status(404);
                res.body(String.format(EXCEPTION_BODY, 404, ex.getMessage()));
            });

            exception(AccountNotFoundException.class, (ex, req, res) -> {
                int statusCode = "post".equalsIgnoreCase(req.requestMethod()) ? 400 : 404;
                res.status(statusCode);
                res.body(String.format(EXCEPTION_BODY, statusCode, ex.getMessage()));
            });

            exception(InsufficientFundsException.class, (ex, req, res) -> {
                res.status(400);
                res.body(String.format(EXCEPTION_BODY, 400, ex.getMessage()));
            });

            exception(InvalidAmountValueException.class, (ex, req, res) -> {
                res.status(400);
                res.body(String.format(EXCEPTION_BODY, 400, ex.getMessage()));
            });
        });
    }

}
