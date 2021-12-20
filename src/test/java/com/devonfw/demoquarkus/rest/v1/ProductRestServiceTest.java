package com.devonfw.demoquarkus.rest.v1;

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

import com.devonfw.quarkus.general.rest.security.ApplicationAccessControlConfig;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
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
  void getNonExistingTest() {

    given().when().contentType(MediaType.APPLICATION_JSON).get("/products/doesnoexist").then().log().all()
        .statusCode(500).extract().response();
  }

  @Test
  @TestSecurity(user = "testuser", roles = { ApplicationAccessControlConfig.PERMISSION_SAVE_PRODUCT })
  @Order(3)
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
  @Order(4)
  public void testGetById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(200)
        .body("title", equalTo("Bose Acoustimass 5 Series III Speaker System - AM53BK"))
        .body("price", equalTo(Float.valueOf(399)));
  }

  @Test
  @TestSecurity(user = "testuser", roles = { ApplicationAccessControlConfig.PERMISSION_DELETE_PRODUCT })
  @Order(5)
  public void deleteById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/products/1").then().statusCode(200)
        .body("title", equalTo("Bose Acoustimass 5 Series III Speaker System - AM53BK"))
        .body("price", equalTo(Float.valueOf(399F)));

    // after deletion it should be deleted
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(500);

  }

}