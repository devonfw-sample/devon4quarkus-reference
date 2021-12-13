package com.devonfw.quarkus.productmanagement.rest.v1.model;

import java.math.BigDecimal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProductDto {

  @Schema(nullable = false, description = "Product title", minLength = 3, maxLength = 500)
  private String title;

  @Schema(description = "Product description", minLength = 3, maxLength = 500)
  private String description;

  @Schema(description = "Product price")
  private BigDecimal price;

}
