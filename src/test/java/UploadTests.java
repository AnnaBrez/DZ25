import config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UploadTests {

    @Test
    public void testFileUpload() {
        File fileToUpload = new File("src/test/resources/testfile.txt");

        given()
                .multiPart("file", fileToUpload)
                .contentType(ContentType.MULTIPART)
                .when()
                .post(ApiConfig.BASE_URL + "/pet/1/uploadImage")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("code", equalTo(200))
                .body("message", containsString("File uploaded"));
    }

    @Test
    public void testFileDownload() {
        // Сначала загружаем файл
        File fileToUpload = new File("src/test/resources/testfile.txt");
        String uploadPath = given()
                .multiPart("file", fileToUpload)
                .post(ApiConfig.BASE_URL + "/pet/1/uploadImage")
                .path("message");

        // Затем скачиваем и проверяем
        byte[] downloadedFile = given()
                .when()
                .get(uploadPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asByteArray();

        assertTrue(downloadedFile.length > 0);
    }
}
