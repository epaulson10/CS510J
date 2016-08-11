package edu.pdx.cs410J.erik.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.erik.client.Appointment;
import edu.pdx.cs410J.erik.client.AppointmentBook;
import edu.pdx.cs410J.erik.client.AppointmentBookService;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The server-side implementation of the AppointmentBook service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{

    // The intention here is to make this okay to use in real-life
    ConcurrentHashMap<String, AppointmentBook> apptBookMap = new ConcurrentHashMap<>();


    /**
     * Get an appointment book by owner
     *
     * @param owner The owner of the desired {@link edu.pdx.cs410J.erik.client.AppointmentBook}
     * @return The {@link edu.pdx.cs410J.erik.client.AppointmentBook} in question.
     */
    @Override
    public AppointmentBook getAppointmentBook(String owner) {
        if (null == owner) return null;
        return apptBookMap.get(owner);
    }

    /**
     * Create an {@link edu.pdx.cs410J.erik.client.AppointmentBook} for the given owner.
     * @param owner
     */
    @Override
    public void createAppointmentBook(String owner) {
        apptBookMap.putIfAbsent(owner, new AppointmentBook(owner));
    }

    /**
     * Add an {@link edu.pdx.cs410J.erik.client.Appointment} to someone's {@link edu.pdx.cs410J.erik.client.AppointmentBook}
     *
     * @param owner The owner of the AppointmentBook
     * @param description The appointment description
     * @param beginTime The start time of the appointment
     * @param endTime The end time of the appointment
     * @return true on success, false otherwise.
     * @throws IllegalArgumentException
     */
    @Override
    public boolean addAppointment(String owner, String description, String beginTime, String endTime) throws IllegalArgumentException{
        if (owner == null) return false;

        apptBookMap.putIfAbsent(owner, new AppointmentBook(owner));
        AppointmentBook book = apptBookMap.get(owner);

        // The constructor for Appointment will throw an IllegalArgumentException if the times are in the wrong format.
        book.addAppointment(new Appointment(description,beginTime,endTime));
        return true;
    }


    /**
     * Search someone's {@link edu.pdx.cs410J.erik.client.AppointmentBook} by time.
     * @param owner The owner of the AppointmentBook
     * @param beginTime The start of the search time.
     * @param endTime The end of the search time.
     * @return An {@link edu.pdx.cs410J.erik.client.AppointmentBook} containing all appointments that fall within those
     * two times.
     * @throws IllegalArgumentException
     */
    @Override
    public AppointmentBook searchAppointments(String owner, String beginTime, String endTime) throws IllegalArgumentException {
        AppointmentBook book = getAppointmentBook(owner);

        Collection<Appointment> apptCollection = book.getAppointments();
        AppointmentBook tempApptBook = new AppointmentBook(owner);
        Date beginDate = Appointment.parseStringIntoDate(beginTime);
        Date endDate = Appointment.parseStringIntoDate(endTime);
        for (Appointment appt : apptCollection) {
            if (appt.getBeginTime().compareTo(beginDate) >= 0 && appt.getEndTime().compareTo(endDate) <= 0) {
                tempApptBook.addAppointment(appt);
            }
        }

        return tempApptBook;
    }


    @Override
    protected void doUnexpectedFailure(Throwable unhandled) {
        unhandled.printStackTrace(System.err);
        super.doUnexpectedFailure(unhandled);
    }

}
