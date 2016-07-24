package edu.pdx.cs410J.erik;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 */
public class PrettyPrinterTest {
    AppointmentBook testBook = new AppointmentBook("Steve");
    ByteArrayOutputStream outMock = new ByteArrayOutputStream();
    File file = new File("test15141.txt");
    @Before
    public void setup() {
        System.setOut(new PrintStream(outMock));
        testBook.addAppointment(new Appointment("ASDF", "01/01/2016 08:00 am", "01/01/2016 09:00 am"));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        System.setOut(null);
        file.delete();
    }

    @Test
    public void canPrintToStandardOut() {
        PrettyPrinter printer = new PrettyPrinter(System.out);
        try {
            printer.dump(testBook);
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage(), false);
        }

        assertThat(outMock.toString(), containsString("Appointment Book Owner: Steve"));
        assertThat(outMock.toString(), containsString("ASDF"));
        assertThat(outMock.toString(), containsString("Starts at: Jan 1, 2016 8:00:00 AM"));
        assertThat(outMock.toString(), containsString("Ends at: Jan 1, 2016 9:00:00 AM"));
        assertThat(outMock.toString(), containsString("Duration in minutes: 60"));
    }

    @Test
    public void canPrintToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrettyPrinter printer = new PrettyPrinter(fos);
            printer.dump(testBook);
            fos.close();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Assert.assertTrue(bufferedReader.lines().anyMatch( s -> s.equals("Appointment Book Owner: Steve")));
            Assert.assertTrue(bufferedReader.lines().anyMatch( s -> s.equals("ASDF")));
            Assert.assertTrue(bufferedReader.lines().anyMatch( s -> s.equals("Starts at: Jan 1, 2016 8:00:00 AM")));
            Assert.assertTrue(bufferedReader.lines().anyMatch( s -> s.equals("Ends at: Jan 1, 2016 9:00:00 AM")));
            Assert.assertTrue(bufferedReader.lines().anyMatch( s -> s.equals("Duration in minutes: 60")));
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage(), false);
        }
    }
}
