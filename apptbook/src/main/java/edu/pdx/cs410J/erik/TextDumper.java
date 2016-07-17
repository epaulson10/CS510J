package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class that implements the {@link AppointmentBookDumper} interface, used to store
 * an {@link AppointmentBook} to a text file.
 */
public class TextDumper implements AppointmentBookDumper {
    File textFile;

    /**
     * Initializes the TextDumper to output the contents of Appointment books to the given filename.
     *
     * @param filename The path to the file to write to
     */
    public TextDumper(String filename) {
        textFile = new File(filename);
    }

    @Override
    public void dump(AbstractAppointmentBook appointmentBook) throws IOException {
        // The constructor to printWriter will throw an exception if the file doesn't exist.
        PrintWriter writer = new PrintWriter(textFile);

        String owner = appointmentBook.getOwnerName();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>(appointmentBook.getAppointments());

        writer.println(owner);

        for (Appointment appt : appointments) {
            writer.println(appt.getDescription());
            writer.println(appt.getBeginTimeString());
            writer.println(appt.getEndTimeString());
        }
        writer.close();
    }


}
