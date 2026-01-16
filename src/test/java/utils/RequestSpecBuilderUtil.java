package utils;
import config.EnvManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecBuilderUtil {

    public static RequestSpecification getRequestSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(EnvManager.getEnv().baseUrl())
                // Add Authorization header globally
                //.addHeader("Authorization", "Bearer " + TokenManager.getToken())
                .build();
    }
}
