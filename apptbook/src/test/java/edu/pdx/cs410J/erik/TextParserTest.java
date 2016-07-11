package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.ParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
public class TextParserTest {

    final static String FILENAME = "test123414.txt";
    TextParser parser = null;
    TextDumper dumper = null;

    AppointmentBook testApptBook = null;

    @Before
    public void setUp() {
        dumper = new TextDumper(FILENAME);
        testApptBook = Utils.generateRandomAppointmentBook("Westley", 400);
        try {
            dumper.dump(testApptBook);
        } catch(IOException e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        parser = new TextParser(FILENAME);
    }

    @Test
    public void parserMatchesApptBookThatCreateTheTextFile() {
        AbstractAppointmentBook parsedBook = null;
        try {
             parsedBook = parser.parse();
        } catch (ParserException e) {
            Assert.assertTrue(e.getMessage(), false);
        }

        assertThat(parsedBook.getOwnerName(), equalTo(testApptBook.getOwnerName()));
        ArrayList<Appointment> initialAppts = new ArrayList<>(testApptBook.getAppointments());

        // Scan through the list of appointments read from the file and as we consider each appointment, find it in the
        // original list and remove it. After we are done scanning the list that came from the file, the original list
        // should also be empty
        Collection parsedAppointments =  parsedBook.getAppointments();
        for (Object parsedObj : parsedAppointments) {
            Appointment appt = (Appointment)parsedObj;
            for (int i = 0; i < initialAppts.size(); i++) {
                if (initialAppts.get(i).equals(appt)) {
                    initialAppts.remove(i);
                    break;
                }
            }
        }
        assertThat(initialAppts.size(), equalTo(0));
    }

}
