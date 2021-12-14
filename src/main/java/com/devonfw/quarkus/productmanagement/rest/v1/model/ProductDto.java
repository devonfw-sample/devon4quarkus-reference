package com.devonfw.quarkus.productmanagement.rest.v1.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

  private Long id;

  private String title;

  private String description;

  private BigDecimal price;

}
