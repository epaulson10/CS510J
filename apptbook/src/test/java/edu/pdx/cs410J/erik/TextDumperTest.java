package edu.pdx.cs410J.erik;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;


import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by erpaulso on 7/9/2016.
 */
public class TextDumperTest {
    // Obscure filename so the possibility of a conflict with an existing file is small
    final static String FILENAME = "test12435326.txt";
    final static String OWNER = "Archduke Franz Ferdinand";
    final static String DESCRIPTION = "Sarajevo Visit";
    final static String START = "06/28/1914 08:00";
    final static String END = "06/28/1914 17:00";
    final static String[] arrayOfTargetStrings = {OWNER, DESCRIPTION, START, END};

    TextDumper dumper;
    AppointmentBook book;
    @Before
    public void setUp() {
        dumper = new TextDumper(FILENAME);
        book = new AppointmentBook(OWNER);
        book.addAppointment(new Appointment(DESCRIPTION, START, END));
    }

    @Test
    public void dumpCreatesATextFileWithTheAppointment() {

        try {
            dumper.dump(book);
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage(), false);
        }

        File file = new File(FILENAME);
        Assert.assertTrue("File not created by dumper.dump", file.exists());

        Scanner scanner = null;

        // Somewhat ineffecient but trying to avoid doing all this looping would
        // add a bunch more code
        for (String target : arrayOfTargetStrings) {
            try {
                scanner = new Scanner(file);
            } catch (IOException e) {
                Assert.assertTrue(e.getMessage(), false);
            }

            boolean foundTarget = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.indexOf(target) != -1) {
                    foundTarget = true;
                    break;
                }
            }
            assertThat("Found " + target, foundTarget);
        }
        // Clean up
        scanner.close();
        file.delete();
    }

}
