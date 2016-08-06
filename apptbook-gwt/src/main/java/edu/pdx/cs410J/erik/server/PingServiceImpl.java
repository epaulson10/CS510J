package edu.pdx.cs410J.erik.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.erik.client.Appointment;
import edu.pdx.cs410J.erik.client.AppointmentBook;
import edu.pdx.cs410J.erik.client.PingService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The server-side implementation of the division service
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{

    ConcurrentHashMap<String, AppointmentBook> apptBookMap = new ConcurrentHashMap<>();

    @Override
    public AppointmentBook ping() {
        AppointmentBook book = new AppointmentBook("Erik");
        book.addAppointment(new Appointment("asdf","11/11/2016 9:00 am","11/11/2016 11:00 am"));
        return book;
    }

    @Override
    public AppointmentBook getAppointmentBook(String owner) {
        if (null == owner) return null;
        return apptBookMap.get(owner);
    }

    @Override
    public void createAppointmentBook(String owner) {
        apptBookMap.putIfAbsent(owner, new AppointmentBook(owner));
    }

    @Override
    public boolean addAppointment(String owner, String description, String beginTime, String endTime) {
        if (owner == null) return false;

        apptBookMap.putIfAbsent(owner, new AppointmentBook(owner));
        AppointmentBook book = apptBookMap.get(owner);

        book.addAppointment(new Appointment(description,beginTime,endTime));
        return true;
    }

    @Override
    public AppointmentBook searchAppointments(String owner, String beginTime, String endTime) {
        return null;
    }


    @Override
    protected void doUnexpectedFailure(Throwable unhandled) {
        unhandled.printStackTrace(System.err);
        super.doUnexpectedFailure(unhandled);
    }

}
