package com.devonfw.quarkus.productmanagement.logic;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

public interface UcFindProduct {
  Page<ProductDto> findProducts(ProductSearchCriteriaDto dto);

  Page<ProductDto> findProductsByCriteriaApi(ProductSearchCriteriaDto dto);

  Page<ProductDto> findProductsByQueryDsl(ProductSearchCriteriaDto dto);

  Page<ProductDto> findProductsByTitleQuery(ProductSearchCriteriaDto dto);

  Page<ProductDto> findProductsByTitleNativeQuery(ProductSearchCriteriaDto dto);

  Page<ProductDto> findProductsOrderedByTitle();

  ProductDto findProduct(String id);

  ProductDto findProductByTitle(String title);
}
