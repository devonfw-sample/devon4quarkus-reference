package com.devonfw.quarkus.exceptionutils;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class BaseTest extends Assertions {

  private static boolean initialSetup = false;

  /**
   * Initializes this test class and resets {@link #isInitialSetup() initial setup flag}.
   */
  @BeforeClass
  public static void setUpClass() {

    initialSetup = true;
  }

  /**
   * Suggests to use {@link #doSetUp()} method before each tests.
   */
  @Before
  public final void setUp() {

    doSetUp();
    if (initialSetup) {
      initialSetup = false;
    }
  }

  /**
   * Suggests to use {@link #doTearDown()} method before each tests.
   */
  @After
  public final void tearDown() {

    doTearDown();
  }

  /**
   * @return {@code true} if this JUnit class is invoked for the first time (first test method is called), {@code false}
   *         otherwise (if this is a subsequent invocation).
   */
  protected boolean isInitialSetup() {

    return initialSetup;
  }

  /**
   * Provides initialization previous to the creation of the text fixture.
   */
  protected void doSetUp() {

  }

  /**
   * Provides clean up after tests.
   */
  protected void doTearDown() {

  }
}
