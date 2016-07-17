package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

/**
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

    private String owner;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /**
     * Constructor for the AppointmentBook
     *
     * @param owner The owner of this appointment book
     */
    public AppointmentBook(String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwnerName() {
        return this.owner;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
}
