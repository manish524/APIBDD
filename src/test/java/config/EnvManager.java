package config;

public class EnvManager {

    private static Environment environment;

    static {
        String env = System.getProperty("env");

        try {
            environment = Environment.valueOf(env);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                "Invalid environment passed: " + env +
                ". Allowed values: DEV, QA, STAGE, PROD"
            );
        }
    }

    public static Environment getEnv() {
        return environment;
    }
}

