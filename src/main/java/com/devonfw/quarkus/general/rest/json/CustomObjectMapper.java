package com.devonfw.quarkus.general.rest.json;

import javax.inject.Singleton;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.quarkus.jackson.ObjectMapperCustomizer;

@Singleton
public class CustomObjectMapper implements ObjectMapperCustomizer {

  @Override
  public void customize(ObjectMapper objectMapper) {

    SimpleModule module = new SimpleModule();
    module.addSerializer(Page.class, new PageSerializer());
    objectMapper.registerModule(module);
  }

}
