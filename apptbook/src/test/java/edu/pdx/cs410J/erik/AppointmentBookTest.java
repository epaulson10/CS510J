package edu.pdx.cs410J.erik;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import static org.junit.Assert.*;


public class AppointmentBookTest {

    private AppointmentBook testBook;
    private static final String OWNER_NAME = "Jim Bob";

    @Before
    public void setUp() throws Exception {
        testBook = new AppointmentBook(OWNER_NAME);
        assertNotNull(testBook);
    }

    @Test
    public void getOwnerName() throws Exception {
        assertEquals(testBook.getOwnerName(), OWNER_NAME);
    }

    @Test
    public void getAppointmentsReturnsACollection() throws Exception {
        assertTrue("getAppointments didn't return a Collection!", testBook.getAppointments() instanceof Collection);
    }

    @Test
    public void addAppointment() throws Exception {
        assertTrue(testBook.getAppointments().size() == 0);
        for (int i = 1; i < 10; i++) {
            testBook.addAppointment(new Appointment("A useless meeting", "12:00", "13:00"));
            assertThat(testBook.getAppointments().size(), is(equalTo((i))));
        }
    }

}