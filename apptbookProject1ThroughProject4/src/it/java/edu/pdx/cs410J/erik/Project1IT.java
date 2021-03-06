package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project3} main class.
 */
public class Project1IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
  }

   private String existingTextFile = "src/test/testFiles/project2TestFile.txt";
    private String emptyTextFile = "src/test/testFiles/empty.txt";
    byte[] fileBytes = null;

    @Before
    public void setup() {
        try {
            // We need to back up the file before we modify it so this test remains valid after the first run
            File fileToBackup = new File(existingTextFile);
            FileInputStream inputStream = new FileInputStream(fileToBackup);
            fileBytes = new byte[(int) fileToBackup.length()];
            inputStream.read(fileBytes);
            inputStream.close();

            // False means overwrite the file. true would have meant append to it
            FileOutputStream outputStream = new FileOutputStream(fileToBackup, false);
            outputStream.write(fileBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);
        }
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
    MainMethodResult result = invokeMain("EazyE", "A stupid meeting", "01-01-1111", "13:00","am", "01-01-1111", "14:00", "am");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr().trim(), equalTo(Project3.DATE_WRONG_ERROR));
  }

  @Test
  public void testReadmeOptionOnlyPrintsReadme() {
    MainMethodResult result = invokeMain("-README", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");
    assertThat(result.getExitCode(), equalTo(0));
    // Trimming the result is needed as we use println instead of print
    assertThat(result.getOut().trim(), equalTo(Project3.README));

  }

  @Test
  public void testPrintOptionPrintsDescription() {
    MainMethodResult result = invokeMain("-print", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");
    assertThat(result.getOut(), containsString("Armistice Agreement Meeting from 11/11/18 until 11/11/18"));
  }

  @Test
  public void testPrettyToStdOutOption() {
    MainMethodResult result = invokeMain("-pretty", "-", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");
    assertThat(result.getOut(), containsString("Starts at: "));
      assertThat(result.getOut(), containsString("Ends at: "));
      assertThat(result.getOut(), containsString("Duration in minutes: "));

      result = invokeMain("-pretty", "-", "-print", "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");
    assertThat(result.getOut(), containsString("Starts at: "));
      assertThat(result.getOut(), containsString("Ends at: "));
      assertThat(result.getOut(), containsString("Duration in minutes: "));
  }
  @Test
  public void textFileOptionShouldCreateATextFile() {
    String fileName = "myTestFile12312341.txt";
      File file = new File(fileName);
      if (file.exists()) {
          file.delete();
      }
    MainMethodResult result = invokeMain("-textFile", fileName, "Triple Entente",
            "Armistice Agreement Meeting", "11/11/1918", "11:11", "pm", "11/11/1918", "12:11", "pm");
    assertThat(file.exists(), equalTo(true));

    //Cleanup
    file.delete();
  }

    @After
    public void tearDown() {
        try {
            File fileToBackup = new File(existingTextFile);
            // False means overwrite the file. true would have meant append to it
            FileOutputStream outputStream = new FileOutputStream(fileToBackup, false);
            outputStream.write(fileBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void canReadAndUpdateAnExistingTextFile() {

        try {
            MainMethodResult result = invokeMain("-textFile", existingTextFile, "-print", "Triple Entente",
                    "Open conflict", "09/02/1939", "11:11", "am", "09/02/1939", "11:59", "pm");
            // Verify that print is still working with other options
            assertThat(result.getOut(), containsString("Open conflict"));
            Scanner scanner = new Scanner(new File(existingTextFile));
            boolean foundWhatIAmLookingFor = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("Open conflict")) {
                    foundWhatIAmLookingFor = true;
                }
            }

            assertThat(foundWhatIAmLookingFor, equalTo(true));
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void readingAnEmptyFileReturnsAnError() {
         MainMethodResult result = invokeMain("-textFile", emptyTextFile, "Triple Entente",
                "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");

        assertThat(result.getExitCode(), not(equalTo(0)));
        assertThat(result.getErr(), containsString("Incorrectly formatted"));

    }

    @Test
    public void ownerNameMismatchIsAnError() {
     MainMethodResult result = invokeMain("-textFile", existingTextFile, "Westley the Doge",
                "Armistice Agreement Meeting", "11/11/1918", "11:11", "am", "11/11/1918", "12:11", "pm");

        assertThat(result.getExitCode(), not(equalTo(0)));
        assertThat(result.getErr(), containsString("Owner name doesn't match the owner name in the text file."));
    }

}
