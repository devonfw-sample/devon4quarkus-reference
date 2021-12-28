package com.devonfw.quarkus.productmanagement.domain.repo;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

public interface ProductFragment {

  public Page<ProductEntity> findByCriteria(ProductSearchCriteriaDto searchCriteria);
}
