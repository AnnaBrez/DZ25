
import config.ApiConfig;
import models.ApiResponse;
import models.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserApiTests {

    private static final String USERNAME = "ann_brez";
    private User testUser;

    @BeforeEach
    public void setup() {
        baseURI = ApiConfig.BASE_URL;

        testUser = User.builder()
                .id(1)
                .username(USERNAME)
                .firstName("ann")
                .lastName("brez")
                .email("test@mail.ru")
                .password("123")
                .phone("66667888")
                .userStatus(1)
                .build();
    }

    @Test
    public void testCreateUserJson() {

        given()
                .contentType("application/json")
                .body(testUser)
                .when()
                .post(ApiConfig.USER_PATH)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", matchesPattern("\\d+"));
    }

    @Test
    public void testGetUserJson() {
        createTestUser();

        given()
                .pathParam("username", USERNAME)
                .when()
                .get(ApiConfig.USER_PATH + "/{username}")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user.json"))
                .body("username", equalTo(USERNAME))
                .body("email", equalTo(testUser.getEmail()));
    }

    @Test
    public void testUpdateUser() {

        createTestUser();


        User updatedUser = User.builder()
                .id(testUser.getId())
                .username(USERNAME)
                .firstName("ann")
                .lastName("nbrez")
                .email("new@mail.ru")
                .password("n123")
                .phone("9876543210")
                .userStatus(2)
                .build();


        given()
                .contentType("application/json")
                .pathParam("username", USERNAME)
                .body(updatedUser)
                .when()
                .put(ApiConfig.USER_PATH + "/{username}")
                .then()
                .statusCode(200);


        User actualUser = given()
                .pathParam("username", USERNAME)
                .when()
                .get(ApiConfig.USER_PATH + "/{username}")
                .as(User.class);

        assertEquals(updatedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(updatedUser.getEmail(), actualUser.getEmail());
        assertEquals(updatedUser.getPhone(), actualUser.getPhone());
    }

    @Test
    public void testDeleteUser() {

        createTestUser();


        ApiResponse deleteResponse = given()
                .pathParam("username", USERNAME)
                .when()
                .delete(ApiConfig.USER_PATH + "/{username}")
                .as(ApiResponse.class);

        assertEquals(200, deleteResponse.getCode());
        assertEquals("unknown", deleteResponse.getType());

        given()
                .pathParam("username", USERNAME)
                .when()
                .get(ApiConfig.USER_PATH + "/{username}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateUserData() {
        User invalidUser = User.builder()
                .username(" ")
                .build();

        given()
                .contentType("application/json")
                .body(invalidUser)
                .when()
                .post(ApiConfig.USER_PATH)
                .then()
                .statusCode(200);
    }

    private void createTestUser() {
        given()
                .contentType("application/json")
                .body(testUser)
                .when()
                .post(ApiConfig.USER_PATH);
    }
}

