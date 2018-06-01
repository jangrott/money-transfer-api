package pl.jangrot.mtransfer.rest;

import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import pl.jangrot.mtransfer.App;
import pl.jangrot.mtransfer.dao.AbstractDaoIntegrationTest;
import spark.Spark;

@RunWith(Cucumber.class)
public class APIAcceptanceTest {

    @BeforeClass
    public static void start() {
        AbstractDaoIntegrationTest.init();

        String[] args = {};
        App.main(args);
    }

    @AfterClass
    public static void stop() {
        Spark.stop();
        AbstractDaoIntegrationTest.tearDown();
    }
}
