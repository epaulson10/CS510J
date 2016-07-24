package edu.pdx.cs410J.erik;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 */
public class Utils {
    public static AppointmentBook generateRandomAppointmentBook(String ownerName, int numberAppointments) {
        AppointmentBook returnVal = new AppointmentBook(ownerName);
        Appointment[] appointments = generateAppointments(numberAppointments);
        for (Appointment appt : appointments) {
            returnVal.addAppointment(appt);
        }
        return returnVal;
    }

    public static Appointment[] generateAppointments(int numToGenerate) {
        // mm/dd/yyyy hh:mm
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy h:m a");
        Random random = new Random();
        Appointment[] appointments = new Appointment[numToGenerate];
        for (int i = 0; i < numToGenerate; i++) {
            // Generate random dates based off of number of seconds since epoch
            String date1 = simpleDateFormat.format(new Date(Math.abs(random.nextLong())));
            String date2 = simpleDateFormat.format(new Date(Math.abs(random.nextLong())));
            appointments[i] = new Appointment(String.valueOf(i), date1, date2);
        }

        return appointments;
    }
}
