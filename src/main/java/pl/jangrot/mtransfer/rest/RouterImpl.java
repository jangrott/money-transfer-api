package pl.jangrot.mtransfer.rest;

import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.service.ClientService;

import javax.inject.Inject;

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
            path("/client", () -> get("/", (req, res) -> clientService.getAll()));
        });
    }

}
