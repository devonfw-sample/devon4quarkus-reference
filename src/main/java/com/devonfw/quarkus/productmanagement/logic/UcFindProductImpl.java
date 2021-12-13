package com.devonfw.quarkus.productmanagement.logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.rest.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

import lombok.extern.slf4j.Slf4j;

@Named
@Transactional
@Slf4j
public class UcFindProductImpl implements UcFindProduct {
  @Inject
  ProductRepository ProductRepository;

  @Inject
  ProductMapper mapper;

  @Override
  public Page<ProductDto> findProducts(ProductSearchCriteriaDto dto) {

    Iterable<ProductEntity> productsIterator = this.ProductRepository.findAll();
    List<ProductEntity> products = new ArrayList<ProductEntity>();
    productsIterator.forEach(products::add);
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByCriteriaApi(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.ProductRepository.findAllCriteriaApi(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByQueryDsl(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.ProductRepository.findAllQueryDsl(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.ProductRepository.findByTitleQuery(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleNativeQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.ProductRepository.findByTitleNativeQuery(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsOrderedByTitle() {

    List<ProductEntity> products = this.ProductRepository.findAllByOrderByTitle().getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto);
  }

  @Override
  public ProductDto findProduct(String id) {

    ProductEntity product = this.ProductRepository.findById(Long.valueOf(id)).get();
    if (product != null) {
      return this.mapper.map(product);
    } else {
      return null;
    }
  }

  @Override
  public ProductDto findProductByTitle(String title) {

    ProductEntity product = this.ProductRepository.findByTitle(title);
    if (product != null) {
      return this.mapper.map(product);
    } else {
      return null;
    }
  }
}