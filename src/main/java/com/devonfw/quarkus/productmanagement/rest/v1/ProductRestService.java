package com.devonfw.quarkus.productmanagement.rest.v1;

import static com.devonfw.quarkus.productmanagement.utils.StringUtils.isEmpty;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;

import java.util.Optional;

import javax.inject.Inject;

import javax.validation.Valid;

import javax.ws.rs.BeanParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.rest.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;

@Path("/product/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper productMapper;

  @Context
  UriInfo uriInfo;

  @GET
  public Page<ProductDto> getAllOrderedByTitle() {

    Page<ProductEntity> products = this.productRepository.findAllByOrderByTitle();
    if (products.isEmpty()) {
      return Page.empty();
    }
    return this.productMapper.map(products);
  }

  @POST
  public Response createNewProduct(ProductDto product) {

    if (isEmpty(product.getTitle())) {
      throw new WebApplicationException("Title was not set on request.", 400);
    }

    ProductEntity productEntity = this.productRepository.save(this.productMapper.map(product));

    UriBuilder uriBuilder = this.uriInfo.getAbsolutePathBuilder().path(Long.toString(productEntity.getId()));
    return created(uriBuilder.build()).build();
  }

  @POST
  @Path("search")
  public Page<ProductDto> findProducts(ProductSearchCriteriaDto searchCriteria) {

    Page<ProductEntity> products = this.productRepository.findByCriteria(searchCriteria);
    if (products.isEmpty()) {
      return Page.empty();
    }
    return this.productMapper.map(products);
  }

  @GET
  @Path("{id}")
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    Optional<ProductEntity> product = this.productRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.productMapper.map(product.get());
    }
    return null;
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.productMapper.map(this.productRepository.findByTitle(title));
  }

  @DELETE
  @Path("{id}")
  public Response deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    this.productRepository.deleteById(Long.valueOf(id));
    return status(Status.NO_CONTENT.getStatusCode()).build();
  }

}
