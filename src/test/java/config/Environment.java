package config;

public enum Environment {

    DEV("https://dev.gorest.co.in/public/v2"),
    QA("https://gorest.co.in/public/v2"),
    STAGE("https://stage.gorest.co.in/public/v2"),
    PROD("https://gorest.co.in/public/v2");

    private final String baseUrl;

    Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String baseUrl() {
        return baseUrl;
    }
}

