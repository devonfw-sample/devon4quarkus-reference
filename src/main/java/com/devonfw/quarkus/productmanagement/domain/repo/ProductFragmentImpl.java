package com.devonfw.quarkus.productmanagement.domain.repo;

import static com.devonfw.quarkus.productmanagement.utils.StringUtils.isEmpty;
import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.model.QProductEntity;
import com.devonfw.quarkus.productmanagement.rest.v1.model.ProductSearchCriteriaDto;
import com.devonfw.quarkus.productmanagement.utils.QueryUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

public class ProductFragmentImpl implements ProductFragment {

  @Inject
  EntityManager em;

  @Override
  public Page<ProductEntity> findByCriteria(ProductSearchCriteriaDto searchCriteria) {

    QProductEntity product = QProductEntity.productEntity;
    List<Predicate> predicates = new ArrayList<>();
    if (!isEmpty(searchCriteria.getTitle())) {
      predicates.add(product.title.eq(searchCriteria.getTitle()));
    }
    if (!isNull(searchCriteria.getPrice())) {
      predicates.add(product.price.eq(searchCriteria.getPrice()));
    } else {
      if (!isNull(searchCriteria.getPriceMin())) {
        predicates.add(product.price.gt(searchCriteria.getPriceMin()));
      }
      if (!isNull(searchCriteria.getPriceMax())) {
        predicates.add(product.price.lt(searchCriteria.getPriceMax()));
      }
    }
    JPAQuery<ProductEntity> query = new JPAQuery<ProductEntity>(this.em).from(product);
    if (!predicates.isEmpty()) {
      query.where(predicates.toArray(Predicate[]::new));
    }
    Pageable pageable = PageRequest.of(searchCriteria.getPageNumber(), searchCriteria.getPageSize());
    query.orderBy(product.title.desc());
    return QueryUtil.findPaginated(pageable, query, searchCriteria.isDetermineTotal());
  }

}
