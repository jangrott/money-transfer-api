package pl.jangrot.mtransfer;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.dao.ClientDaoImpl;
import pl.jangrot.mtransfer.rest.Router;
import pl.jangrot.mtransfer.rest.RouterImpl;
import pl.jangrot.mtransfer.service.ClientService;
import pl.jangrot.mtransfer.service.ClientServiceImpl;

@Slf4j
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientDao.class).to(ClientDaoImpl.class);
        bind(ClientService.class).to(ClientServiceImpl.class);
        bind(Router.class).to(RouterImpl.class);
    }
}
