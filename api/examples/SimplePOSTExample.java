package examples;

import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class SimplePOSTExample {
    final static String BASE_URI = "https://petstore.swagger.io/v2/pet";

    @Test
    public void AddNewPet() {
        // Write the request body
        String reqBody = "{\"id\": 77232, \"name\": \"Riley\", \"status\": \"alive\"}";

    Response response =
            given().contentType(ContentType.JSON) // Set headers
                    .body(reqBody).when().post(BASE_URI); // Send POST request

    // Print response of POST request
    String body = response.getBody().asPrettyString();
    System.out.println(body);
    }
}
