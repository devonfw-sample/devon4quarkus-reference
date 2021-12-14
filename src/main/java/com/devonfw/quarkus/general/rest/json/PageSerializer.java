package com.devonfw.quarkus.general.rest.json;

import java.io.IOException;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PageSerializer extends JsonSerializer<Page> {

  @Override
  public void serialize(Page page, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    if (page != null) {
      gen.writeStartObject();

      gen.writeObjectField("content", page.getContent());
      gen.writeObjectFieldStart("pageable");
      gen.writeObjectFieldStart("sort");
      gen.writeBooleanField("unsorted", page.getSort().isUnsorted());
      gen.writeBooleanField("sorted", page.getSort().isSorted());
      gen.writeBooleanField("empty", page.getSort().isEmpty());
      gen.writeEndObject();
      gen.writeNumberField("offset", page.getPageable().getOffset());
      gen.writeNumberField("pageSize", page.getPageable().getPageSize());
      gen.writeNumberField("pageNumber", page.getPageable().getPageNumber());
      gen.writeBooleanField("paged", page.getPageable().isPaged());
      gen.writeBooleanField("unpaged", page.getPageable().isUnpaged());
      gen.writeEndObject();
      gen.writeBooleanField("last", page.isLast());
      gen.writeNumberField("totalPages", page.getTotalPages());
      gen.writeNumberField("totalElements", page.getTotalElements());
      gen.writeBooleanField("first", page.isFirst());
      gen.writeNumberField("numberOfElements", page.getNumberOfElements());
      gen.writeObjectFieldStart("sort");
      gen.writeBooleanField("unsorted", page.getSort().isUnsorted());
      gen.writeBooleanField("sorted", page.getSort().isSorted());
      gen.writeBooleanField("empty", page.getSort().isEmpty());
      gen.writeEndObject();
      gen.writeNumberField("number", page.getNumber());
      gen.writeNumberField("size", page.getSize());
      gen.writeBooleanField("empty", page.isEmpty());

      gen.writeEndObject();
    }
  }

}
