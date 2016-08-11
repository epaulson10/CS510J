package edu.pdx.cs410J.erik.server;

import com.google.gwt.junit.client.GWTTestCase;
import edu.pdx.cs410J.erik.client.Appointment;
import edu.pdx.cs410J.erik.client.AppointmentBook;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppointmentBookServiceImplTest extends GWTTestCase{

    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();

    public void testCreateAppointmentBookCreatesEmptyApptBook (){
        service.createAppointmentBook("Johnny");

        AppointmentBook book = service.getAppointmentBook("Johnny");

        assertNotNull(book);

        assertThat(book.getAppointments().size(), equalTo(0));
    }

    public void testAddAppointment() {
        service.addAppointment("Johnny", "An appointment", "01/01/2016 08:00 am", "01/01/2016 09:00 am");
        Collection<Appointment> appts = service.getAppointmentBook("Johnny").getAppointments();

        boolean found = false;
        for (Appointment appt : appts) {
            if (appt.getDescription().equals("An appointment")) {
                found = true;
            }
        }

        assertTrue(found);
    }

    public void testSearchAppointments() {
        service.addAppointment("Johnny", "1234", "01/02/2016 09:00 am", "01/02/2016 11:00 am");
        AppointmentBook foundAppts = service.searchAppointments("Johnny", "01/02/2016 09:00 am", "01/02/2016 12:00 pm");
        assertTrue(foundAppts.getAppointments().size() == 1);
        assertTrue(foundAppts.getAppointments().stream().anyMatch(appointment -> appointment.getDescription().equals("1234")));

    }

    @Override
    public String getModuleName() {
        return null;
    }
}
