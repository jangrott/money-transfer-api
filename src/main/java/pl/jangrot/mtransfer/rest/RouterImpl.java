package pl.jangrot.mtransfer.rest;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.exception.AccountNotFoundException;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.service.ClientAccountService;

import javax.inject.Inject;
import java.util.UUID;

import static spark.Spark.*;

@Slf4j
public class RouterImpl implements Router {

    private static final String EMPTY = "";
    private static final String EXCEPTION_BODY = "{ \"statusCode\": %d, \"message\": \"%s\"}";

    private final Gson gson;
    private final ClientAccountService clientAccountService;

    @Inject
    public RouterImpl(Gson gson, ClientAccountService clientAccountService) {
        this.gson = gson;
        this.clientAccountService = clientAccountService;
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

                exception(ClientNotFoundException.class, (ex, req, res) -> {
                    res.status(404);
                    res.body(String.format(EXCEPTION_BODY, 404, ex.getMessage()));
                });

                exception(AccountNotFoundException.class, (ex, req, res) -> {
                    res.status(404);
                    res.body(String.format(EXCEPTION_BODY, 404, ex.getMessage()));
                });
            });
        });
    }

}
