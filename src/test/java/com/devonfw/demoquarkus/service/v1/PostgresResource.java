package com.devonfw.demoquarkus.service.v1;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgresResource implements QuarkusTestResourceLifecycleManager {

  static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:11.5").withDatabaseName("demo_db")
      .withUsername("demo").withPassword("demo");

  @Override
  public Map<String, String> start() {

    database.start();
    return Collections.singletonMap("quarkus.datasource.url", database.getJdbcUrl());
  }

  @Override
  public void stop() {

    database.stop();
  }
}