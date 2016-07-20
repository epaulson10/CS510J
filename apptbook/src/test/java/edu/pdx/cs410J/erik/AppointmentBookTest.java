package edu.pdx.cs410J.erik;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

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
            testBook.addAppointment(new Appointment("A useless meeting", "12/12/2016 12:00 pm", "12/12/2016 1:00 pm"));
            assertThat(testBook.getAppointments().size(), is(equalTo((i))));
        }
    }

    @Test
    public void appointmentsAreSorted() {
        AppointmentBook book = new AppointmentBook(OWNER_NAME);
        Appointment appt4 = new Appointment("b", "01/01/2015 08:00 am", "01/01/2015 09:00 am");
        Appointment appt2 = new Appointment("a", "01/01/2016 07:00 am", "01/01/2016 09:00 am");
        Appointment appt3 = new Appointment("a", "01/01/2016 08:00 am", "01/01/2016 08:30 am");
        Appointment appt = new Appointment("a", "01/01/2016 08:00 am", "01/01/2016 09:00 am");
        Appointment appt1 = new Appointment("b", "01/01/2016 08:00 am", "01/01/2016 09:00 am");

        ArrayList<Appointment> appts = new ArrayList<>();
        appts.add(appt); appts.add(appt1);appts.add(appt2);appts.add(appt3);appts.add(appt4);

        // Add appointments to a book in random order. They should be sorted when we view them later
        Random r = new Random();
        while (!appts.isEmpty()) {
            int nextAppt = r.nextInt(appts.size());
            book.addAppointment(appts.remove(nextAppt));
        }

        ArrayList<Appointment> sorted =  (ArrayList<Appointment>)book.getAppointments();
        Assert.assertTrue(sorted.get(0).equals(appt4));
        Assert.assertTrue(sorted.get(1).equals(appt2));
        Assert.assertTrue(sorted.get(2).equals(appt3));
        Assert.assertTrue(sorted.get(3).equals(appt));
        Assert.assertTrue(sorted.get(4).equals(appt1));

    }

}