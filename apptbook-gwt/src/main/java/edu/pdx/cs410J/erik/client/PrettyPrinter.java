package edu.pdx.cs410J.erik.client;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

/**
 * Allows for dumping a textual representation of an AppointmentBook to a OutputStream.
 */
public class PrettyPrinter {

    OutputStream outputStream;

    /**
     * Constructor for PrettyPrinter
     *
     * @param outputStream The outputstream to write the pretty text to.
     */
    PrettyPrinter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public static String getPrettyString(AppointmentBook book) {
        StringBuilder builder = new StringBuilder();
        builder.append("Appointment Book Owner: ").append(book.getOwnerName()).append('\n');

        for (Appointment appointment : (Collection<Appointment>) book.getAppointments()) {
            Date start = appointment.getBeginTime();
            Date end = appointment.getEndTime();
            // getTime returns the number of milliseconds since the Unix Epoch
            long durationInMinutes = (end.getTime() - start.getTime()) / (1000 * 60);
            builder.append(appointment.getDescription()).append('\n');
            builder.append("Starts at: ").append(Appointment.parseDateIntoString(start)).append('\n');
            builder.append("Ends at: ").append(Appointment.parseDateIntoString(end)).append('\n');
            builder.append("Duration in minutes: ").append(durationInMinutes).append("\n\n");
        }
        return builder.toString();
    }
}
