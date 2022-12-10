package com.restful.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class RestAs {
    private static String requestBodyUpdate = "{\n" +
            "  \"title\": \"foo\",\n" +
            "  \"completed\": \"baz\",\n" +
            "  \"userId\": \"1\",\n" +
            "  \"id\": \"3\" \n}";

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    //    @Test
    @org.testng.annotations.Test(priority = 1)
    public void getRequest() {
        ValidatableResponse validate = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then();

        Response response = validate.extract().response();
        JsonPath jsonPathEvaluator = response.jsonPath();
        assertEquals(200, response.statusCode());
        assertEquals(jsonPathEvaluator.getList("").size(), 200);
        assertEquals("qui ullam ratione quibusdam voluptatem quia omnis", response.jsonPath().getString("title[5]"));
    }

    //    @Test
    @org.testng.annotations.Test(priority = 2)
    public void getRequestWithQueryParam() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "JWT todos")
                .param("users", "2")
                .when()
                .get("/todos")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertEquals("quo adipisci enim quam ut ab", response.jsonPath().getString("title[7]"));
    }

    //    @Test
    @org.testng.annotations.Test(priority = 3)
    public void postRequest() {
        String jsonData = "{\r\n"
                + "    \"title\": \"text title\",\r\n"
                + "    \"completed\": \"false\",\r\n"
                + "    \"userId\": \"2\"\r\n"
                + "}";
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonData)
                .when()
                .post("/todos")
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertEquals("text title", response.jsonPath().getString("title"));
        assertEquals("false", response.jsonPath().getString("completed"));
        assertEquals("2", response.jsonPath().getString("userId"));
        assertEquals("201", response.jsonPath().getString("id"));
    }

    //    @Test
    @org.testng.annotations.Test(priority = 4)
    public void putRequest() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBodyUpdate)
                .when()
                .put("/todos/1")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertEquals("foo", response.jsonPath().getString("title"));
        assertEquals("baz", response.jsonPath().getString("completed"));
        assertEquals("1", response.jsonPath().getString("userId"));
        assertEquals("1", response.jsonPath().getString("id"));
    }

    @Test
    public void deleteRequest() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .when()
                .delete("/todos/1")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
    }
}
