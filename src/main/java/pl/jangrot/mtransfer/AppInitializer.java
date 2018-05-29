package pl.jangrot.mtransfer;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import pl.jangrot.mtransfer.rest.Router;

public class AppInitializer {
    @Inject
    public AppInitializer(PersistService persistService, Router router) {
        persistService.start();
        router.start();
    }
}
