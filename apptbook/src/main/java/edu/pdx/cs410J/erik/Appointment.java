package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {

  private String description;
  private String beginTime;
  private String endTime;

  /**
   * Constructor for an Appointment
   *
   * @param description The description for this Appointment
   * @param beginTime The start time for this appointment in 24-hour time (example: 7/15/2016 14:39)
   * @param endTime The end time for this appointment in 24-hour time (example: example: 7/15/2016 14:39)
     */
  public Appointment(String description, String beginTime, String endTime) {
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }



  @Override
  public String getBeginTimeString() {
    return beginTime;
  }

  @Override
  public String getEndTimeString() {
    return endTime;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
