package edu.pdx.cs410J.erik.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

public class Appointment extends AbstractAppointment implements Comparable<Appointment>{

    private String description;
    private Date beginTime;
    private Date endTime;

    /**
     * Constructor for an Appointment
     *
     * @param description The description for this Appointment
     * @param beginTime   The start time for this appointment in 24-hour time (example: 7/15/2016 14:39)
     * @param endTime     The end time for this appointment in 24-hour time (example: example: 7/15/2016 14:39)
     */
    public Appointment(String description, String beginTime, String endTime) {
        this.description = description;
        this.beginTime = parseStringIntoDate(beginTime);
        this.endTime = parseStringIntoDate(endTime);
    }

     /**
     * Constructor for an Appointment
     *
     * @param description The description for this Appointment
     * @param beginTime   The start time for this appointment as a {@link Date}
     * @param endTime     The end time for this appointment as a {@link Date}
     */
    public Appointment(String description, Date beginTime, Date endTime) {
        this.description = description;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Appointment() {
    }

    /**
     * Get the starting time of this Appointment as a {@link Date}
     * @return A {@link Date} representing the start of this appointment.
     */
    @Override
    public Date getBeginTime() {
        // Use the copy constructor to avoid returning our internal reference
        return new Date(beginTime.getTime());
    }

    /**
     * Get the ending time of this Appointment as a {@link Date}
     * @return A {@link Date} representing the end of this appointment.
     */
    @Override
    public Date getEndTime() {
        // Use the copy constructor to avoid returning our internal reference
        return new Date(endTime.getTime());
    }

    /**
     * Returns a string representation of the start of this Appointment.
     * @return A string representation of the start of this Appointment.
     */
    @Override
    public String getBeginTimeString() {
        //return DateTimeFormat.getShortDateFormat().format(beginTime);
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dateTimeFormat = new DateTimeFormat("MM/dd/yyyy", info) {};
        return dateTimeFormat.format(beginTime);
    }

    /**
     * Returns a string representation of the end of this Appointment.
     * @return A string representation of the end of this Appointment.
     */
    @Override
    public String getEndTimeString() {
        //return DateTimeFormat.getShortDateFormat().format(endTime);
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dateTimeFormat = new DateTimeFormat("MM/dd/yyyy", info) {};
        return dateTimeFormat.format(beginTime);
    }

    /**
     * Getter for the description string.
     *
     * @return A string representing the description for this Appointment
     */
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
        } else {
            return false;
        }
    }

    /**
     * Helper function that parses a string into a Date
     * @param dateString The string to parse
     * @return A {@link Date} that was parsed from the string.
     */
    static Date parseStringIntoDate(String dateString) {
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dateTimeFormat = new DateTimeFormat("MM/dd/yyyy hh:mm a", info) {};

        return dateTimeFormat.parse(dateString);
    }

    /**
     * Compares this Appointment to the given Appointment. An Appointment is less than another if it starts first.
     * If two Appointments start at the same time, then the one that ends first is less than the other. If the start
     * and end times for the two appointments are the same, then the appointment with the lexigraphically less
     * description is less than the other.
     *
     * @param appointment The Appointment to compare this Appointment to
     *
     * @return Less than 0 if this Appointment is less-than the given Appointment. Greater than 0 if this Appointment
     * is greater than the given Appointment. 0 if the two Appointments are equal
     */
    @Override
    public int compareTo(Appointment appointment) {
        int beginningComp = this.beginTime.compareTo(appointment.beginTime);
        if (beginningComp != 0) return beginningComp;
        int endingComp = this.endTime.compareTo(appointment.endTime);
        if (endingComp != 0) return endingComp;
        int descComp = this.description.compareTo(appointment.description);
        return descComp;
    }
}
