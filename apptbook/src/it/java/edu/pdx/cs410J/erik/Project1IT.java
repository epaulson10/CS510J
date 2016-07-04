package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project1} main class.
 */
public class Project1IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project1} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project1.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString("Missing command line arguments"));
  }

  @Test
  public void testIncorrectDateNotAccepted () {
    MainMethodResult result = invokeMain("EazyE", "A stupid meeting", "13:00", "14:00");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr().trim(), equalTo(Project1.DATE_WRONG_ERROR));
  }

  @Test
  public void testReadmeOptionOnlyPrintsReadme() {
    MainMethodResult result = invokeMain("-readme", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918 11:11", "11/11/1918 12:11");
    assertThat(result.getExitCode(), equalTo(0));
    // Trimming the result is needed as we use println instead of print
    assertThat(result.getOut().trim(), equalTo(Project1.README));

  }

  @Test
  public void testPrintOptionPrintsDescription() {
    MainMethodResult result = invokeMain("-print", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918 11:11", "11/11/1918 12:11");
    assertThat(result.getOut(), containsString("Armistice Agreement Meeting from 11/11/1918 11:11 until 11/11/1918 12:11"));
  }

}