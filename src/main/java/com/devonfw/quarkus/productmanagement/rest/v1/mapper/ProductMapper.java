package com.devonfw.quarkus.productmanagement.rest.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.rest.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;

//mapstruct will generate an impl class(CDI bean, see pom.xml) from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface ProductMapper {

  ProductDto map(ProductEntity model);

  ProductEntity create(NewProductDto dto);

  List<ProductDto> map(List<ProductEntity> Products);
}
