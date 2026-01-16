package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import payloads.request.CreateUserRequest;
import services.UserService;
import utils.TestContext;
import utils.TestListener;

import java.util.Map;

import client.RestClient;

import static org.testng.Assert.*;

public class UserSteps {

    private Response response;
    private JsonPath jsonPath;
    private CreateUserRequest createUserRequest;

    private final UserService userService = new UserService();

    // ==============================
    // GIVEN – PAYLOAD
    // ==============================

    @Given("user payload with")
    public void user_payload_with(DataTable dataTable) {

        Map<String, String> userData =
                dataTable.asMaps(String.class, String.class).get(0);

        String email = userData.get("email").trim();

        createUserRequest = new CreateUserRequest(
                userData.get("name").trim(),
                email,
                userData.get("gender").trim().toLowerCase(),
                userData.get("status").trim().toLowerCase()
        );

        TestListener.getTest().info(
        	    "<b>GIVEN : User payload created</b><br>" +
        	    "Name : " + createUserRequest.getName() + "<br>" +
        	    "Email : " + createUserRequest.getEmail() + "<br>" +
        	    "Gender : " + createUserRequest.getGender() + "<br>" +
        	    "Status : " + createUserRequest.getStatus()
        	);

    }

    @Given("invalid user payload missing {string}")
    public void invalid_user_payload_missing(String field) {

        createUserRequest =
                new CreateUserRequest("Test", "test@test.com", "male", "active");

        switch (field.toLowerCase()) {
            case "name":
                createUserRequest.setName(null);
                break;
            case "email":
                createUserRequest.setEmail(null);
                break;
            case "gender":
                createUserRequest.setGender(null);
                break;
            case "status":
                createUserRequest.setStatus(null);
                break;
        }

        TestListener.getTest().info(
                "GIVEN : Invalid payload created (missing field = " + field + ")"
        );
    }

    // ==============================
    // AUTHORIZATION
    // ==============================

    @And("authorization token is available")
    public void authorization_token_is_available() {
        TestListener.getTest().info("AND : Authorization token is available");
    }

    @And("no authorization token")
    public void no_authorization_token() {
        RestClient.clearInvalidToken();
        TestListener.getTest().info("AND : No authorization token provided");
    }

    @And("invalid authorization token")
    public void invalid_authorization_token() {
        RestClient.setInvalidToken("invalidtoken123");
        TestListener.getTest().info("AND : Invalid authorization token used");
    }

    // ==============================
    // WHEN – API CALLS
    // ==============================

    @When("client sends POST request to {string}")
    public void client_sends_post_request(String endpoint) {

        TestListener.getTest().info(
                "WHEN : Sending POST request to " + endpoint
        );

        response = userService.post(endpoint, createUserRequest);
        jsonPath = response.jsonPath();

        if (response.getStatusCode() == 201) {
            TestContext.userId = jsonPath.getInt("id");
            TestListener.getTest().info(
                    "Captured user_id for chaining = " + TestContext.userId
            );
        }
    }

    @When("client sends POST request to {string} without token")
    public void client_sends_post_without_token(String endpoint) {

        TestListener.getTest().info(
                "WHEN : Sending POST request WITHOUT TOKEN to " + endpoint
        );

        response = userService.postWithoutAuth(endpoint, createUserRequest);
        jsonPath = response.jsonPath();
    }

    @When("client sends PUT request to {string} with")
    public void client_sends_put_request_with(String endpoint,
                                              DataTable dataTable) {

        TestListener.getTest().info(
                "WHEN : Sending PUT request for user_id = " + TestContext.userId
        );

        Map<String, String> updateData =
                dataTable.asMaps(String.class, String.class).get(0);

        String email = updateData.get("email");
        String[] parts = email.trim().toLowerCase().split("@");

        String uniqueEmail =
                parts[0] + System.currentTimeMillis() + "@" +
                (parts.length > 1 ? parts[1] : "test.com");

        CreateUserRequest updateUser =
                new CreateUserRequest(
                        updateData.get("name").trim(),
                        uniqueEmail,
                        updateData.get("gender").trim().toLowerCase(),
                        updateData.get("status").trim().toLowerCase()
                );

        response = userService.put(
                endpoint.replace("{user_id}",
                        String.valueOf(TestContext.userId)),
                updateUser
        );

        jsonPath = response.jsonPath();
    }

    @When("client sends GET request to {string}")
    public void client_sends_get_request(String endpoint) {

        String finalEndpoint =
                endpoint.replace("{user_id}",
                        String.valueOf(TestContext.userId));

        TestListener.getTest().info(
                "WHEN : Sending GET request to " + finalEndpoint
        );

        response = userService.get(finalEndpoint);
        jsonPath = response.jsonPath();
    }

    @When("client sends DELETE request to {string}")
    public void client_sends_delete_request(String endpoint) {

        String finalEndpoint =
                endpoint.replace("{user_id}",
                        String.valueOf(TestContext.userId));

        TestListener.getTest().info(
                "WHEN : Sending DELETE request to " + finalEndpoint
        );

        response = userService.delete(finalEndpoint);
        jsonPath = response.jsonPath();
    }

    // ==============================
    // THEN – ASSERTIONS
    // ==============================

    @Then("the response status code should be {int}")
    public void verify_status_code(int statusCode) {

        assertEquals(response.getStatusCode(), statusCode);

        TestListener.getTest().pass(
                "THEN : Verified response status code = " + statusCode
        );
    }

    @And("the response should contain the created user details")
    public void verify_created_user() {

        assertEquals(jsonPath.getString("name"),
                createUserRequest.getName());

        assertEquals(jsonPath.getString("gender"),
                createUserRequest.getGender());

        assertEquals(jsonPath.getString("status"),
                createUserRequest.getStatus());

        assertTrue(
                jsonPath.getString("email")
                        .startsWith(
                                createUserRequest
                                        .getEmail()
                                        .split("@")[0]
                        )
        );

        TestListener.getTest().pass(
                "THEN : Created user details verified successfully"
        );
    }

    @And("the response should contain updated user details")
    public void verify_updated_user() {
        verify_created_user();
        TestListener.getTest().pass(
                "THEN : Updated user details verified"
        );
    }

    @And("the response should contain a list of users")
    public void verify_user_list() {

        assertTrue(jsonPath.getList("$").size() > 0);

        TestListener.getTest().pass(
                "THEN : User list is present and not empty"
        );
    }

    @And("the response should contain user id {int}")
    public void verify_user_id(int id) {

        assertEquals(jsonPath.getInt("id"), id);

        TestListener.getTest().pass(
                "THEN : Response contains expected user id = " + id
        );
    }

    @Then("the response should contain an error message")
    public void verify_error_message() {

        assertTrue(jsonPath.getList("field").size() > 0);
        assertTrue(jsonPath.getList("message").size() > 0);

        TestListener.getTest().pass(
                "THEN : Error message verified in response"
        );
    }

    @Then("the response should contain an {string} as message")
    public void the_response_message_should_contain(String expectedMessage) {

        assertTrue(
                jsonPath.getString("message")
                        .contains(expectedMessage)
        );

        TestListener.getTest().pass(
                "THEN : Response message contains → " + expectedMessage
        );
    }

    @And("the response should contain user details")
    public void the_response_should_contain_user_details() {

        assertNotNull(jsonPath.get("id"));
        assertNotNull(jsonPath.get("name"));
        assertNotNull(jsonPath.get("email"));
        assertNotNull(jsonPath.get("gender"));
        assertNotNull(jsonPath.get("status"));

        TestListener.getTest().pass(
                "THEN : User details are present in response"
        );
    }
}
