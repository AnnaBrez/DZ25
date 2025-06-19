package config;

import static io.restassured.RestAssured.given;

public class ApiConfig {
    public static final String BASE_URL = "https://petstore.swagger.io/v2";
    public static final String USER_PATH = "/user";
    public static final String PET_PATH = "/pet";
    public static final String STORE_PATH = "/store";
    public static final String AUTH_PATH = "/user/login";

    public static String getAuthToken(String username, String password) {
        return given()
                .contentType("application/json")
                .body(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password))
                .when()
                .post(BASE_URL + AUTH_PATH)
                .then()
                .extract()
                .path("token");
    }
}
