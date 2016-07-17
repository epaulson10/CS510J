package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment extends AbstractAppointment {

  private String description;
  private Date beginTime;
  private Date endTime;
  DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);

  /**
   * Constructor for an Appointment
   *
   * @param description The description for this Appointment
   * @param beginTime The start time for this appointment in 24-hour time (example: 7/15/2016 14:39)
   * @param endTime The end time for this appointment in 24-hour time (example: example: 7/15/2016 14:39)
     */
  public Appointment(String description, String beginTime, String endTime) {
    this.description = description;
    this.beginTime = parseStringIntoDate(beginTime);
    this.endTime = parseStringIntoDate(endTime);
  }

  @Override
  public Date getBeginTime() {
    // Use the copy constructor to avoid returning our internal reference
    return new Date(beginTime.getTime());
  }

  @Override
  public Date getEndTime() {
    // Use the copy constructor to avoid returning our internal reference
    return new Date(endTime.getTime());
  }

  @Override
  public String getBeginTimeString() {
    return dateFormatter.format(beginTime);
  }

  @Override
  public String getEndTimeString() {
    return dateFormatter.format(endTime);

  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Checks for equality between two appointments by comparing the description, start time, and end time strings
   * for equality.
   *
   * @param appointment The appointment to compare this appointment to
   * @return true if the appointments are equal, false otherwise.
     */
  public boolean equals(Appointment appointment) {
    if (null == appointment) {
      return false;
    }

    if (this.beginTime.equals(appointment.beginTime)
            && this.endTime.equals(appointment.endTime)
            && this.description.equals(appointment.description)) {
      return true;
    }
    else {
      return false;
    }
  }

  private Date parseStringIntoDate(String dateString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    ParsePosition position = new ParsePosition(0);

    Date date = simpleDateFormat.parse(dateString, position);

    return date;
  }
}
