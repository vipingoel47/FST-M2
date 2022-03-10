package pactProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.asserts.Assertion;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Create map for headers
    Map<String, String> headers = new HashMap<>();
    //API route
    String createUser = "/api/users";

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        //Add header values
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        //Create request and response body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        //Create pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                    .path(createUser)
                    .headers(headers)
                    .method("POST")
                    .body(requestResponseBody)
                .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest(){
        //API URL
        RestAssured.baseURI = "http://localhost:8282";

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("firstName", "Andrew");
        requestBody.put("lastName", "Hall");
        requestBody.put("email", "andrewhall@mail.com");

        //Response Builder
        Response response = RestAssured.given().headers(headers).when().body(requestBody).post(createUser);

        //Assertion
        response.then().statusCode(201);
    }
}
