package com.devonfw.quarkus.general.rest.security;

public class ApplicationAccessControlConfig {

  public static final String APP_ID = "devon4quarkus-product";

  private static final String PREFIX = APP_ID + ".";

  public static final String PERMISSION_FIND_PRODUCT = PREFIX + "FindProduct";

  public static final String PERMISSION_SAVE_PRODUCT = PREFIX + "SaveProduct";

  public static final String PERMISSION_DELETE_PRODUCT = PREFIX + "DeleteProduct";
}