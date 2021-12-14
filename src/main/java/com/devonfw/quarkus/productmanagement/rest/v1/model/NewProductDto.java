package com.devonfw.quarkus.productmanagement.rest.v1.model;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProductDto {

  @Size(min = 3, max = 500)
  private String title;

  @Size(min = 3, max = 4000)
  private String description;

  private BigDecimal price;

}
