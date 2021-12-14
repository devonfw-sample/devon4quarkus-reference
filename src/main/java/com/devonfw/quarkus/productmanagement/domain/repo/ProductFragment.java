package com.devonfw.quarkus.productmanagement.domain.repo;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

public interface ProductFragment {
  public Page<ProductEntity> findAllCriteriaApi(ProductSearchCriteriaDto dto);

  public Page<ProductEntity> findAllQueryDsl(ProductSearchCriteriaDto dto);

  public Page<ProductEntity> findByTitleNativeQuery(ProductSearchCriteriaDto dto);

  public Page<ProductEntity> findByTitleQuery(ProductSearchCriteriaDto dto);
}
