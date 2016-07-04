package edu.pdx.cs410J.erik;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

  @Test
  public void getBeginTimeStringIsImplemented() {
    Appointment appointment = new Appointment("A waste-of-time staff meeting", "12:00", "13:00");
    assertThat(appointment.getBeginTimeString(), containsString("12:00"));
  }

  @Test
  public void getEndTImeStringIsImplemented() {
    Appointment appointment = new Appointment("A waste-of-time staff meeting", "12:00", "13:00");
    assertThat(appointment.getEndTimeString(), containsString("13:00"));
  }

  @Test
  public void differentAppointmentsReturnDifferentDescriptions() {
    Appointment appointment = new Appointment("asdf", "12", "13");
    Appointment appointment1 = new Appointment("A real sentence", "12", "13");
    assertThat(appointment.getDescription(), is(not(equalTo(appointment1.getDescription()))));
  }

  @Test
  public void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    Appointment appointment = new Appointment("asdf", "12", "13");
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
