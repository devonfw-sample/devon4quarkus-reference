package com.devonfw.quarkus.productmanagement.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devonfw.quarkus.general.service.exception.InvalidParameterException;
import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

import lombok.extern.slf4j.Slf4j;

@Named
@Transactional
@Slf4j
public class UcFindProductImpl implements UcFindProduct {
  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper mapper;

  @Override
  public Page<ProductDto> findProducts(ProductSearchCriteriaDto dto) {

    Iterable<ProductEntity> productsIterator = this.productRepository.findAll();
    List<ProductEntity> products = new ArrayList<ProductEntity>();
    productsIterator.forEach(products::add);
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByCriteriaApi(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.productRepository.findAllCriteriaApi(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByQueryDsl(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.productRepository.findAllQueryDsl(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.productRepository.findByTitleQuery(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleNativeQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> products = this.productRepository.findByTitleNativeQuery(dto).getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), productsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsOrderedByTitle() {

    List<ProductEntity> products = this.productRepository.findAllByOrderByTitle().getContent();
    List<ProductDto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto);
  }

  @Override
  public ProductDto findProduct(String id) {

    if (!StringUtils.isNumeric(id)) {
      throw new InvalidParameterException("Unable to parse ID: " + id);
    }

    Optional<ProductEntity> product = this.productRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.mapper.map(product.get());
    } else {
      return null;
    }
  }

  @Override
  public ProductDto findProductByTitle(String title) {

    Optional<ProductEntity> product = this.productRepository.findByTitle(title);
    if (product.isPresent()) {
      return this.mapper.map(product.get());
    } else {
      return null;
    }
  }
}