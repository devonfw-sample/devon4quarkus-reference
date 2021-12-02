package com.devonfw.demoquarkus.service.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;

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

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/products").then().statusCode(200)
        .extract().response();

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

    Response response = given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/products").then()
        .log().all().statusCode(200).header("Location", nullValue()).extract().response();

    assertEquals(200, response.statusCode());

    response = given().when().contentType(MediaType.APPLICATION_JSON).get("/products").then().log().all()
        .statusCode(200).extract().response();

    // number of elements is 351, because there are already 350 products in the database
    int products = Integer.valueOf(response.jsonPath().getString("totalElements"));
    assertEquals(351, products);

    List<LinkedHashMap<String, String>> created = response.jsonPath().getList("content");
    assertNotNull(created);
    assertEquals(product.getTitle(), created.get(350).get("title"));
  }

  @Test
  @Order(3)
  public void testGetById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(200)
        .body("title", equalTo("Bose Acoustimass 5 Series III Speaker System - AM53BK"))
        .body("price", equalTo(Float.valueOf(399)));
  }

  @Test
  @Order(4)
  public void deleteById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/products/1").then().statusCode(204);

    // after deletion it should be deleted
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(404);

    // delete again should fail
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/products/1").then().statusCode(404);
  }

  @Test
  @Order(5)
  void businessExceptionTest() {

    given().when().contentType(MediaType.APPLICATION_JSON).get("/products/doesnotexist").then().log().all()
        .statusCode(422).extract().response();
  }

  @Test
  @Order(6)
  void notFoundExceptionTest() {

    given().when().contentType(MediaType.APPLICATION_JSON).get("/products/0").then().log().all().statusCode(404)
        .extract().response();
  }

  @Test
  @Order(7)
  void validationExceptionTest() {

    // Create a product that does not match the validation rules
    ProductDto product = new ProductDto();
    product.setTitle("");

    given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/products").then().log().all()
        .statusCode(400);

  }

}