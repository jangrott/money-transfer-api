package pl.jangrot.mtransfer.rest;

import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.exception.ClientNotFoundException;
import pl.jangrot.mtransfer.exception.NonUniqueClientException;
import pl.jangrot.mtransfer.service.ClientService;

import javax.inject.Inject;
import java.util.UUID;

import static spark.Spark.*;

@Slf4j
public class RouterImpl implements Router {

    private final ClientService clientService;


    @Inject
    public RouterImpl(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public void start() {
        path("/api", () -> {
            before("/*", (req, res) -> {
                log.info("Received {} request to endpoint: {}", req.requestMethod(), req.uri());
                res.type("application/json");
            });
            path("/clients", () -> {
                get("/", (req, res) -> clientService.getAll());
                get("/:clientId", (req, res) -> {
                    UUID id = UUID.fromString(req.params("clientId"));
                    return clientService.getById(id);
                });

                exception(ClientNotFoundException.class, (ex, req, res) -> {
                    res.status(404);
                    res.body(ex.getMessage());
                });
                exception(NonUniqueClientException.class, (ex, req, res) -> {
                    res.status(400);
                    res.body(ex.getMessage());
                });
            });
        });
    }

}
