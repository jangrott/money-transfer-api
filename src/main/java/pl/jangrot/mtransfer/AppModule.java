package pl.jangrot.mtransfer;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import pl.jangrot.mtransfer.dao.AccountDao;
import pl.jangrot.mtransfer.dao.AccountDaoImpl;
import pl.jangrot.mtransfer.dao.ClientDao;
import pl.jangrot.mtransfer.dao.ClientDaoImpl;
import pl.jangrot.mtransfer.rest.Router;
import pl.jangrot.mtransfer.rest.RouterImpl;
import pl.jangrot.mtransfer.service.ClientAccountService;
import pl.jangrot.mtransfer.service.ClientAccountServiceImpl;
import pl.jangrot.mtransfer.service.InternalTransferService;
import pl.jangrot.mtransfer.service.TransferService;

@Slf4j
public class AppModule extends AbstractModule {

    private static final String UNDERSCORE = "_";

    @Override
    protected void configure() {
        bind(Gson.class).toInstance(new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().startsWith(UNDERSCORE);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create());

        bind(AccountDao.class).to(AccountDaoImpl.class);
        bind(ClientDao.class).to(ClientDaoImpl.class);
        bind(ClientAccountService.class).to(ClientAccountServiceImpl.class);
        bind(TransferService.class).to(InternalTransferService.class);
        bind(Router.class).to(RouterImpl.class);
    }
}
