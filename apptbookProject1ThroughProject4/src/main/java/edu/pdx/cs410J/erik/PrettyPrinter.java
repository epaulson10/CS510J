package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Allows for dumping a textual representation of an AppointmentBook to a OutputStream.
 */
public class PrettyPrinter implements AppointmentBookDumper {

    OutputStream outputStream;

    /**
     * Constructor for PrettyPrinter
     *
     * @param outputStream The outputstream to write the pretty text to.
     */
    PrettyPrinter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Dump the given AppointmentBook to the OutputStream specified in the constructor.
     *
     * @param abstractAppointmentBook The AppointmentBook to dump.
     * @throws IOException
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        PrintWriter writer = new PrintWriter(outputStream);
        StringBuilder builder = new StringBuilder();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        builder.append("Appointment Book Owner: ").append(abstractAppointmentBook.getOwnerName()).append('\n');
        for (Appointment appointment : (Collection<Appointment>) abstractAppointmentBook.getAppointments()) {
            Date start = appointment.getBeginTime();
            Date end = appointment.getEndTime();
            // getTime returns the number of milliseconds since the Unix Epoch
            long durationInMinutes = (end.getTime() - start.getTime()) / (1000 * 60);
            builder.append(appointment.getDescription()).append('\n');
            builder.append("Starts at: ").append(dateFormat.format(start)).append('\n');
            builder.append("Ends at: ").append(dateFormat.format(end)).append('\n');
            builder.append("Duration in minutes: ").append(durationInMinutes).append("\n\n");
        }
        writer.print(builder.toString());
        writer.close();
    }
}
