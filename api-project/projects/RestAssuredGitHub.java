package projects;

import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssuredGitHub {
    // Declare request specification
    RequestSpecification requestSpec;
    // Declare response specification
    ResponseSpecification responseSpec;

    String sshKey;
    int sshID;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                //Set Authorization header that has the value “token <GitHub access token>”
                .addHeader("Authorization", "token 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")
                // Set base URL
                .setBaseUri("https://api.github.com")
                // Build request specification
                .build();
    }

    @Test(priority=1)
    // Add SSH key to GITHUB account
    public void addSSHKey() {
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQClmg1ITbH+QcQJaEq0rHKFCCXaFulGUcKVu/mlNW3LAj5dceVGrh8EcIwJU3p3whqGX1r6Iu/exTR9vDupCpffVMymaG/bW59V6rmzqIdRDffi/6xKMicuPxiMKqbxPl91qKP7luA3/ZhlS2/ClvxPMR/Rzp0Ri7kYXG2m826Pzn+zWtlnLU3lWb+s2MNTCgc5YnzaJVs2giMW5EuvuSK/XL0QmEFDaFZ7/ahu5KDN5DVKFmNUo+pyM7wZ7ClTuoAlembuscf/JLd0euKaOt8JBnsqPVz4Bi0oMzzhqcF+6f/HuQiuCduTnfFEivavniw6/Zl1CH0N5AdLP1iKxSGj\"}";
        Response response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post("/user/keys"); // Send POST request

        sshID = response.jsonPath().getInt("id");
        Reporter.log(response.prettyPrint());

        // Assertions
        response.then().statusCode(201);
    }

    @Test(priority=2)
    // Add SSH key to GITHUB account
    public void getSSHKey() {
        Response response = given().spec(requestSpec) // Use requestSpec
                .when().get("/user/keys"); // Send GET request

        Reporter.log(response.prettyPrint());

        // Assertions
        response.then().statusCode(200);
    }

    @Test(priority=3)
    // Add SSH key to GITHUB account
    public void deleteSSHKey() {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("keyId",sshID)
                .when().delete("/user/keys/{keyId}"); // Send DELETE request

        Reporter.log(response.prettyPrint());

            // Assertions
        response.then().statusCode(204);
    }
}
