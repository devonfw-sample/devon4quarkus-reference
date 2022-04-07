package com.devonfw.demoquarkus.rest.v1;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
@TestMethodOrder(OrderAnnotation.class)
class ProductRestServiceTest {

  @Test
  @Order(1)
  void getAll() {

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1").then().log().all()
        .statusCode(OK.getStatusCode()).extract().response();
    int products = Integer.valueOf(response.jsonPath().getString("totalElements"));
    assertEquals(350, products);
  }

  @Test
  @Order(2)
  void createNewProduct() {

    ProductDto product = new ProductDto();
    product.setTitle("HP Notebook");
    product.setDescription("ZBook");
    product.setPrice(BigDecimal.valueOf(1));
    Response response = given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then()
        .log().all().statusCode(201).extract().response();
    String url = response.header("Location");
    response = given().when().contentType(MediaType.APPLICATION_JSON).get(url).then().log().all().statusCode(200)
        .extract().response();
    assertEquals(product.getTitle(), response.jsonPath().getString("title"));
  }

  @Test
  @Order(3)
  public void testGetById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().statusCode(200)
        .body("title", equalTo("Bose Acoustimass 5 Series III Speaker System - AM53BK"))
        .body("price", equalTo(Float.valueOf(399)));
  }

  @Test
  @Order(4)
  public void deleteById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/product/v1/1").then().statusCode(204);

    // after deletion it should be deleted
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().statusCode(404);

    // delete again should fail
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/product/v1/1").then().statusCode(404);
  }

  @Test
  @Order(5)
  void businessExceptionTest() {

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/doesnotexist").then().log().all()
        .statusCode(422).extract().response();
  }

  @Test
  @Order(6)
  void notFoundExceptionTest() {

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/0").then().log().all().statusCode(404)
        .extract().response();
  }

  @Test
  @Order(7)
  void validationExceptionTest() {

    // Create a product that does not match the validation rules
    ProductDto product = new ProductDto();
    product.setTitle("");

    given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then().log().all()
        .statusCode(400);

  }

}