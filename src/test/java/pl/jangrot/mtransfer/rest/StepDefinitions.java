package pl.jangrot.mtransfer.rest;

import com.google.gson.JsonObject;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pl.jangrot.mtransfer.dao.AbstractDaoIntegrationTest;
import pl.jangrot.mtransfer.model.Account;
import pl.jangrot.mtransfer.model.Client;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static pl.jangrot.mtransfer.util.TestDataGenerator.createClient;
import static pl.jangrot.mtransfer.util.TestUtils.*;

public class StepDefinitions extends AbstractDaoIntegrationTest {

    private static final String RANDOM_CLIENT_ID = "be8bdd99-d68e-48f9-89f4-ad2b066b4ae2";
    private static final String JOHN_DOE_ID_PLACEHOLDER = "<JohnDoeId>";
    private static final String NON_EXISTING_ID_PLACEHOLDER = "<NonExistingId>";
    private static final String JOHN_DOE_ACCOUNT = "<JohnDoeAccount>";
    private static final String JOHN_DOE_ACCOUNT_ID_PLACEHOLDER = "<JohnDoeAccountId>";
    private static final String JANE_DOE_ACCOUNT = "<JaneDoeAccount>";
    private static final String JANE_DOE_ACCOUNT_ID_PLACEHOLDER = "<JaneDoeAccountId>";
    private static final String NON_EXISTING_ACCOUNT_ID_PLACEHOLDER = "<NonExistingAccountId>";
    private static final Long RANDOM_ACCOUNT_ID = 236436253L;

    static {
        RestAssured.baseURI = "http://localhost:4567";
        RestAssured.basePath = "/api";
    }

    private Response response;
    private ValidatableResponse json;

    private List<Client> savedClients;
    private List<Client> clients;

    @Before
    public void setUp() {
        deleteClients();
    }

    @Given("^system contains following clients$")
    public void setupClients(DataTable inputData) {
        clients = inputData.asMaps(String.class, String.class).stream()
                .map(row -> createClient(row.get("firstName"), row.get("lastName")))
                .collect(Collectors.toList());
    }

    @Given("^clients have following accounts$")
    public void setupAccounts(DataTable inputData) {
        inputData.asMaps(String.class, String.class)
                .forEach(row -> {
                    Client client = getClientByFirstAndLastName(clients, row.get("firstName"), row.get("lastName"));
                    Account account = new Account();
                    account.setBalance(toBigDecimal(row.get("accountBalance")));
                    client.getAccounts().add(account);
                });
    }

    @When("^client requests GET (.*)$")
    public void doGetRequest(String path) {
        savedClients = storeClients(clients);
        response = get(buildPath(path));
    }

    @When("^client requests POST /transfer with following body$")
    public void doPostRequest(DataTable inputData) {
        Map<String, String> input = inputData.asMap(String.class, String.class);
        savedClients = storeClients(clients);
        JsonObject json = new JsonObject();
        json.addProperty("fromAccount", resolvePlaceholder(input.get("fromAccount"), Long.class));
        json.addProperty("toAccount", resolvePlaceholder(input.get("toAccount"), Long.class));
        json.addProperty("balance", Float.valueOf(input.get("amount")));

        response = given().body(json).post("/transfer");
    }

    @Then("^response status is (\\d+)$")
    public void checkResponseStatus(int status) {
        json = response.then().statusCode(status);
    }

    @Then("^response contains (\\d+) clients$")
    public void checkResponseSize(int expectedSize) {
        json.body("$", hasSize(expectedSize));
    }

    @Then("^response includes the following clients$")
    public void checkResponseMultipleClients(DataTable expectedData) {
        expectedData.asMaps(String.class, String.class)
                .forEach(expected -> {
                    String firstName = expected.get("firstName");
                    String lastName = expected.get("lastName");
                    json.root(format("[%s]", expected.get("order")))
                            .body("id", equalTo(extractClientId(
                                    getClientByFirstAndLastName(savedClients, firstName, lastName)).toString()))
                            .body("firstName", equalTo(firstName))
                            .body("lastName", equalTo(lastName))
                            .body("accounts", hasSize(0));
                });
    }

    @Then("^response includes the following client$")
    public void checkResponseSingleClient(DataTable expectedData) {
        Map<String, String> expected = expectedData.asMap(String.class, String.class);

        String firstName = expected.get("firstName");
        String lastName = expected.get("lastName");

        json.body("id", equalTo(extractClientId(
                getClientByFirstAndLastName(savedClients, firstName, lastName)).toString()))
                .body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName))
                .body("accounts", hasSize(0));
    }

    @Then("^response includes the following message$")
    public void checkResponseException(DataTable expectedData) {
        Map<String, String> expected = expectedData.asMap(String.class, String.class);

        json.body("statusCode", equalTo(Integer.valueOf(expected.get("statusCode"))))
                .body("message", equalTo(expected.get("message")
                        .replace(NON_EXISTING_ID_PLACEHOLDER, RANDOM_CLIENT_ID)
                        .replace(NON_EXISTING_ACCOUNT_ID_PLACEHOLDER, String.valueOf(RANDOM_ACCOUNT_ID))));
    }

    @Then("^response includes the following accounts$")
    public void checkResponseMultipleAccounts(DataTable expectedData) {
        expectedData.asMaps(String.class, String.class)
                .forEach(expected -> json.root(format("[%s]", expected.get("order")))
                        .body("balance", equalTo(Float.valueOf(expected.get("balance")))));
    }

    @Then("^response includes the following account$")
    public void checkResponseSingleAccount(DataTable expectedData) {
        Map<String, String> expected = expectedData.asMap(String.class, String.class);
        long expectedId = extractAccount(getClientByFirstAndLastName(savedClients, "John", "Doe")).getId();
        json.body("id", equalTo((int) expectedId))
                .body("balance", equalTo(Float.valueOf(expected.get("balance"))));
    }

    @Then("^balance of accounts are following")
    public void checkBalanceOfAccounts(DataTable expectedData) {
        expectedData.asMaps(String.class, String.class).forEach(row ->
                assertThat(resolvePlaceholder(row.get("accountId"), Account.class).getBalance())
                        .isEqualTo(toBigDecimal(row.get("balance"))));
    }

    private String buildPath(String path) {
        if (path.contains(JOHN_DOE_ID_PLACEHOLDER)) {
            path = path.replace(JOHN_DOE_ID_PLACEHOLDER,
                    resolvePlaceholder(JOHN_DOE_ID_PLACEHOLDER, UUID.class).toString());
        }
        if (path.contains(NON_EXISTING_ID_PLACEHOLDER)) {
            path = path.replace(NON_EXISTING_ID_PLACEHOLDER,
                    resolvePlaceholder(NON_EXISTING_ID_PLACEHOLDER, String.class));
        }
        if (path.contains(JOHN_DOE_ACCOUNT_ID_PLACEHOLDER)) {
            path = path.replace(JOHN_DOE_ACCOUNT_ID_PLACEHOLDER,
                    String.valueOf(resolvePlaceholder(JOHN_DOE_ACCOUNT_ID_PLACEHOLDER, Long.class)));
        }
        if (path.contains(NON_EXISTING_ACCOUNT_ID_PLACEHOLDER)) {
            path = path.replace(NON_EXISTING_ACCOUNT_ID_PLACEHOLDER,
                    String.valueOf(resolvePlaceholder(NON_EXISTING_ACCOUNT_ID_PLACEHOLDER, Long.class)));
        }
        return path;
    }

    private <T> T resolvePlaceholder(String placeholder, Class<T> type) {
        switch (placeholder) {
            case JOHN_DOE_ID_PLACEHOLDER:
                return type.cast(extractClientId(getClientByFirstAndLastName(savedClients, "John", "Doe")));
            case JOHN_DOE_ACCOUNT:
                return type.cast(extractAccount(getClientByFirstAndLastName(savedClients, "John", "Doe")));
            case JOHN_DOE_ACCOUNT_ID_PLACEHOLDER:
                return type.cast(extractAccount(getClientByFirstAndLastName(savedClients, "John", "Doe")).getId());
            case JANE_DOE_ACCOUNT:
                return type.cast(extractAccount(getClientByFirstAndLastName(savedClients, "Jane", "Doe")));
            case JANE_DOE_ACCOUNT_ID_PLACEHOLDER:
                return type.cast(extractAccount(getClientByFirstAndLastName(savedClients, "Jane", "Doe")).getId());
            case NON_EXISTING_ID_PLACEHOLDER:
                return type.cast(RANDOM_CLIENT_ID);
            case NON_EXISTING_ACCOUNT_ID_PLACEHOLDER:
                return type.cast(RANDOM_ACCOUNT_ID);
        }
        throw new IllegalStateException("Not known placeholder");
    }
}
