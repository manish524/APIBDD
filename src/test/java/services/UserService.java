package services;

import client.RestClient;
import io.restassured.response.Response;
import payloads.request.CreateUserRequest;

public class UserService {

    // ---------------------------
    // GET request
    // ---------------------------
    public Response get(String endpoint) {
        return RestClient.get(endpoint);
    }

    // ---------------------------
    // POST request with valid token
    // ---------------------------
    public Response post(String endpoint, CreateUserRequest payload) {
        return RestClient.post(endpoint, payload);
    }

    // ---------------------------
    // POST request without token (negative test)
    // ---------------------------
    public Response postWithoutAuth(String endpoint, CreateUserRequest payload) {
        return RestClient.postWithoutAuth(endpoint, payload);
    }

    // ---------------------------
    // PUT request to update a user
    // ---------------------------
    public Response put(String endpoint, CreateUserRequest payload) {
        return RestClient.put(endpoint, payload);
    }

    // ---------------------------
    // DELETE request
    // ---------------------------
    public Response delete(String endpoint) {
        return RestClient.delete(endpoint);
    }

    // ---------------------------
    // Negative auth tests
    // ---------------------------
    public void setInvalidToken(String token) {
        RestClient.setInvalidToken(token);
    }

    public void clearInvalidToken() {
        RestClient.clearInvalidToken();
    }
}
