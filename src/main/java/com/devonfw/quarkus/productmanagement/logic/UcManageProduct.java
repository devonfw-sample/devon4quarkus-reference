package com.devonfw.quarkus.productmanagement.logic;

import com.devonfw.quarkus.productmanagement.rest.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

public interface UcManageProduct {
  ProductDto saveProduct(NewProductDto dto);

  ProductDto deleteProduct(String id);
}
