package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.rest.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.rest.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

@Named
@Transactional
public class UcManageProductImpl implements UcManageProduct {
  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper mapper;

  @Override
  public ProductDto saveProduct(NewProductDto dto) {

    ProductEntity created = this.productRepository.save(this.mapper.create(dto));
    return this.mapper.map(created);
  }

  @Override
  public ProductDto deleteProduct(String id) {

    ProductEntity product = this.productRepository.findById(Long.valueOf(id)).get();
    if (product != null) {
      this.productRepository.delete(product);
      return this.mapper.map(product);
    } else {
      return null;
    }
  }
}
