package com.devonfw.quarkus.productmanagement.rest.v1.model;

import java.math.BigDecimal;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCriteriaDto {

  @QueryParam("title")
  private String title;

  @QueryParam("page")
  @DefaultValue("0")
  private int pageNumber = 0;

  @QueryParam("size")
  @DefaultValue("10")
  private int pageSize = 10;

  @QueryParam("priceMin")
  private BigDecimal priceMin;

  @QueryParam("priceMax")
  private BigDecimal priceMax;

  @QueryParam("price")
  private BigDecimal price;
}
