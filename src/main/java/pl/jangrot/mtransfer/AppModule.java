package pl.jangrot.mtransfer;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.dao.ClientAccountDao;
import pl.jangrot.mtransfer.dao.ClientAccountDaoImpl;
import pl.jangrot.mtransfer.rest.Router;
import pl.jangrot.mtransfer.rest.RouterImpl;
import pl.jangrot.mtransfer.service.ClientAccountService;
import pl.jangrot.mtransfer.service.ClientAccountServiceImpl;

@Slf4j
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientAccountDao.class).to(ClientAccountDaoImpl.class);
        bind(ClientAccountService.class).to(ClientAccountServiceImpl.class);
        bind(Router.class).to(RouterImpl.class);
    }
}
