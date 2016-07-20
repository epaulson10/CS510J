package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    /**
     * Add an {@link Appointment} into this AppointmentBook. Appointments are added in ascending order.
     * To learn more about the ordering of Appointments, see {@link Appointment#compareTo(Appointment)}
     * @param appointment The appointment to add to this appointment book;
     */
    @Override
    public void addAppointment(Appointment appointment) {
        int index = Collections.binarySearch(appointments,appointment);
        if (index < 0)
            appointments.add((-index)-1, appointment);
        else
            appointments.add(index, appointment);
    }
}
