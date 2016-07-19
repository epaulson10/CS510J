package edu.pdx.cs410J.erik;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/*
TODO:
Test having dates with multiple digits (i.e. 9:16 and 11:11)
Test am/pm
 */

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

  public static Appointment testAppt = new Appointment("Armistice Agreement Meeting", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");

  @Test
  public void getBeginTimeStringIsImplemented() {
    assertThat(testAppt.getBeginTimeString(), containsString("11/11/18"));
  }

  @Test
  public void getEndTImeStringIsImplemented() {
    assertThat(testAppt.getEndTimeString(), containsString("11/11/18"));
  }

  @Test
  public void differentAppointmentsReturnDifferentDescriptions() {
    Appointment appointment = new Appointment("Armistice Agreement Meeting", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");
    Appointment appointment1 = new Appointment("Definitely Not An Armistice Agreement Meeting", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");
    assertThat(appointment.getDescription(), is(not(equalTo(appointment1.getDescription()))));
  }

  @Test
  public void getBeginAndEndTimeAreImplemented() {
    Date start = testAppt.getBeginTime();
    Date end = testAppt.getEndTime();
    assertThat(start, is(not(nullValue())));
    assertThat(end, is(not(nullValue())));
    assertThat(start.toString(), containsString("Mon Nov 11 11:11:00 PST 1918"));
    assertThat(end.toString(), containsString("Mon Nov 11 12:11:00 PST 1918"));
  }

  @Test
  public void appointmentIsComparable() {
    Assert.assertTrue(Comparable.class.isAssignableFrom(Appointment.class));
  }

  @Test
  public void testAppointmentComparison() {
    // Test description ordering
    Appointment appointment = new Appointment("A", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");
    Appointment appointment1 = new Appointment("Z", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");
    Appointment appointment2 = new Appointment("Z", "11/11/1918 11:11 am", "11/11/1918 12:11 pm");
    Appointment appointment3 = new Appointment("Z", "11/11/1918 11:11 am", "11/11/1918 13:11 pm");
    Appointment appointment4 = new Appointment("Z", "11/11/1918 11:12 am", "11/11/1918 12:11 pm");

    Assert.assertTrue(appointment.compareTo(appointment1) < 0 );
    Assert.assertTrue(appointment1.compareTo(appointment) > 0);
    Assert.assertTrue(appointment1.compareTo(appointment2) == 0);

    Assert.assertTrue(appointment3.compareTo(appointment) > 0);
    Assert.assertTrue(appointment.compareTo(appointment3) < 0);
    Assert.assertTrue(appointment3.compareTo(appointment3) == 0);

    Assert.assertTrue(appointment4.compareTo(appointment) > 0);
    Assert.assertTrue(appointment.compareTo(appointment4) < 0);
    Assert.assertTrue(appointment4.compareTo(appointment4) == 0);
  }

}
