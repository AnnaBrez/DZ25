import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void testCreateUser() {

        String requestBody = """
                {
                  "id": 1,
                  "username": "ann_brez",
                  "firstName": "ann",
                  "lastName": "brez",
                  "email": "test@mail.ru",
                  "password": "123",
                  "phone": "66667888",
                  "userStatus": 1
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", notNullValue());
    }

    @Test
    public void testGetUser() {
        createTestUser();

        given()
                .pathParam("username", "ann_brez")
                .when()
                .get("/user/{username}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", equalTo("ann_brez"))
                .body("firstName", equalTo("ann"))
                .body("lastName", equalTo("brez"))
                .body("email", equalTo("test@mail.ru"))
                .body("password", equalTo("123"))
                .body("phone", equalTo("66667888"))
                .body("userStatus", equalTo(1));
    }

    @Test
    public void testUpdateUser() {

        createTestUser();

        String updatedBody = """
                {
                  "id": 1,
                  "username": "ann_brez",
                  "firstName": "ann",
                  "lastName": "nbrez",
                  "email": "new@mail.ru",
                  "password": "n123",
                  "phone": "9876543210",
                  "userStatus": 2
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("username", "ann_brez")
                .body(updatedBody)
                .when()
                .put("/user/{username}")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", notNullValue());


        given()
                .pathParam("username", "ann_brez")
                .when()
                .get("/user/{username}")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("ann"))
                .body("email", equalTo("new@mail.ru"))
                .body("password", equalTo("n123"))
                .body("phone", equalTo("9876543210"))
                .body("userStatus", equalTo(2));
    }

    @Test
    public void testDeleteUser() {

        createTestUser();

        given()
                .pathParam("username", "ann_brez")
                .when()
                .delete("/user/{username}")
                .then()
.statusCode(200);


        given()
                .pathParam("username", "ann_brez")
                .when()
                .get("/user/{username}")
                .then()
                .statusCode(404);
    }

    private void createTestUser() {
        String requestBody = """
                {
                  "id": 1,
                  "username": "ann_brez",
                  "firstName": "ann",
                  "lastName": "brez",
                  "email": "test@mail.ru",
                  "password": "123",
                  "phone": "66667888",
                  "userStatus": 1
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user");
    }
}


