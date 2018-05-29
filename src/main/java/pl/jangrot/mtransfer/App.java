package pl.jangrot.mtransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;

public class App {

    private static final String PERSISTENCE_UNIT_NAME = "mtransfer";

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule(), new JpaPersistModule(PERSISTENCE_UNIT_NAME));
        injector.getInstance(AppInitializer.class);

        injector.getInstance(DataGenerator.class);
    }

}
