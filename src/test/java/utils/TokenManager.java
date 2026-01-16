package utils;



import client.RestClient;
import config.ConfigReader;
import io.restassured.response.Response;

public class TokenManager {

    // ThreadLocal ensures thread safety during parallel execution
    private static ThreadLocal<String> token = new ThreadLocal<>();

    private TokenManager() {
        // Prevent object creation
    }

    // Public method to get token
    public static String getToken() {

        // If token already exists, return it
        if (token.get() == null) {
            generateToken();
        }
        return token.get();
    }

    // Call auth API and generate token
    private static void generateToken() {

//        Response response = RestClient.post(
//                ConfigReader.get("auth.base.uri"),
//                ConfigReader.get("auth.endpoint"),
//                ConfigReader.get("auth.payload")
//        );
    	

        String accessToken = ConfigReader.get("auth.token");

        token.set(accessToken);
    }

    // Clears token (used after scenario)
    public static void clearToken() {
        token.remove();
    }
}
