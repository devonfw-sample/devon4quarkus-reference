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

import org.junit.jupiter.api.Test;
import org.tkit.quarkus.test.WithDBData;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;

import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

//Before you run this test, tkit-test extension starts docker containers from resources/docker-compose.yaml
//we get a real postgresdb for our tests which will be stopped after tests. No manual test setup is needed.
@QuarkusTest
@QuarkusTestResource(DockerComposeTestResource.class)
class ProductRestServiceTest {// extends AbstractTest {

  @Test
  // we also started a micro container, that can populated DB with data from excel
  // annotating class or method with @WithDBData allows us to scope data for each test even if we use the same DB
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  void getAll() {

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/products").then().statusCode(200)
        .extract().response();

    int products = Integer.valueOf(response.jsonPath().getString("totalElements"));
    assertEquals(2, products);
  }

  @Test
  void getNonExistingTest() {

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/products/doesnoexist").then().log()
        .all().statusCode(500).extract().response();
  }

  @Test
  @WithDBData(value = "data/empty.xls", deleteBeforeInsert = true)
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

    int products = Integer.valueOf(response.jsonPath().getString("totalElements"));
    assertEquals(1, products);
    List<LinkedHashMap<String, String>> created = response.jsonPath().getList("content");
    assertNotNull(created);
    assertEquals(product.getTitle(), created.get(0).get("title"));
  }

  @Test
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  public void testGetById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(200)
        .body("description", equalTo("Apple Notebook"));
  }

  @Test
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  public void deleteById() {

    // delete
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/products/1").then().statusCode(200)
        .body("title", equalTo("MacBook Pro"));

    // after deletion it should be deleted
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/products/1").then().statusCode(500);

  }

}