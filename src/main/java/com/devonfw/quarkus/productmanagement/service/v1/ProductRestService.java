package com.devonfw.quarkus.productmanagement.service.v1;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.PageImpl;
import org.tkit.quarkus.rs.models.PageResultDTO;

import com.devonfw.quarkus.productmanagement.logic.UcFindProduct;
import com.devonfw.quarkus.productmanagement.logic.UcManageProduct;
import com.devonfw.quarkus.productmanagement.service.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/product")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
  @Context
  UriInfo uriInfo;

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcManageProduct ucManageProduct;

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PagedProductResponse.class))),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "Get Products", description = "Returns list of Products matching given criteria, uses pagination")
  @GET
  // REST service methods should not declare exceptions, any thrown error will be transformed by exceptionMapper in
  // tkit-rest
  // We did not define custom @Path - so it will use class level path
  public PageImpl<ProductDto> getAll(@BeanParam ProductSearchCriteriaDto dto) {

    return (PageImpl) this.ucFindProduct.findProducts(dto);
  }

  @GET
  @Path("search")
  public PageImpl<ProductDto> getProductsByCriteriaApi(@BeanParam ProductSearchCriteriaDto dto) {

    return (PageImpl) this.ucFindProduct.findProductsByCriteriaApi(dto);
  }

  @POST
  @Path("search")
  public PageImpl<ProductDto> getProductsByQueryDsl(@BeanParam ProductSearchCriteriaDto dto) {

    return (PageImpl) this.ucFindProduct.findProductsByQueryDsl(dto);
  }

  @GET
  @Path("searchbytitle")
  public PageImpl<ProductDto> getProductsByTitleQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return (PageImpl) this.ucFindProduct.findProductsByTitleQuery(dto);
  }

  @POST
  @Path("searchbytitle")
  public PageImpl<ProductDto> getAllNativeQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return (PageImpl) this.ucFindProduct.findProductsByTitleNativeQuery(dto);
  }

  @GET
  @Path("searchall")
  public PageImpl<ProductDto> getAllOrderedByTitle() {

    return (PageImpl) this.ucFindProduct.findProductsOrderedByTitle();
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New Product created", content = @Content(schema = @Schema(implementation = NewProductDto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "createNewProduct", description = "Stores new Product in DB")
  @POST
  // We did not define custom @Path - so it will use class level path.
  // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
  public ProductDto createNewProduct(NewProductDto dto) {

    return this.ucManageProduct.saveProduct(dto);
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "getProductById", description = "Returns Product with given id")
  @GET
  @Path("{id}")
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucFindProduct.findProduct(id);
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.ucFindProduct.findProductByTitle(title);
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "deleteProductById", description = "Deletes the Product with given id")
  @DELETE
  @Path("{id}")
  public ProductDto deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucManageProduct.deleteProduct(id);
  }

  private static class PagedProductResponse extends PageResultDTO<ProductDto> {
  }

}
