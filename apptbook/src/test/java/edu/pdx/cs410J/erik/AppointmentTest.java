package edu.pdx.cs410J.erik;

import org.junit.Test;

import java.text.SimpleDateFormat;
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

}
