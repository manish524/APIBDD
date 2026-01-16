package client;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

public class RestClient {

    // ------------------------
    // Token management for negative tests
    // ------------------------
    private static String invalidToken = null;

    /**
     * Use this to set an invalid token for 401 tests
     */
    public static void setInvalidToken(String token) {
        invalidToken = token;
    }

    /**
     * Clear invalid token after negative test
     */
    public static void clearInvalidToken() {
        invalidToken = null;
    }

    // =========================
    // GET
    // =========================
    public static Response get(String endpoint) {
        if (invalidToken != null) {
            // Negative auth scenario
            return given()
            		.contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + invalidToken)
                    .when()
                    .get(endpoint)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }

        // Positive / normal requests → use global requestSpec (Hooks + RequestSpecBuilder)
        return given()
        		.contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    // =========================
    // POST
    // =========================
    public static Response post(String endpoint, Object payload) {
        if (invalidToken != null) {
            return given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + invalidToken)
                    .body(payload)
                    .when()
                    .post(endpoint)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }

        // Positive request → global requestSpec already has baseUri + valid token
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /**
     * POST request explicitly without token (negative test)
     */
    public static Response postWithoutAuth(String endpoint, Object payload) {
        return given()
                .baseUri(ConfigReader.get("base.uri"))
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    // =========================
    // PUT
    // =========================
    public static Response put(String endpoint, Object payload) {
        if (invalidToken != null) {
            return given()
                    .baseUri(ConfigReader.get("base.uri"))
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + invalidToken)
                    .body(payload)
                    .when()
                    .put(endpoint)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }

        return given()
        		.contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    // =========================
    // DELETE
    // =========================
    public static Response delete(String endpoint) {
        if (invalidToken != null) {
            return given()
                    .baseUri(ConfigReader.get("base.uri"))
                    .header("Authorization", "Bearer " + invalidToken)
                    .when()
                    .delete(endpoint)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }

        return given()
        		.contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
