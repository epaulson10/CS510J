package edu.pdx.cs410J.erik;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This servlet provides a REST API for working with an
 * <code>AppointmentBook</code>. You are able to create and query appointments with it.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private static final String OWNER_NAME = "owner";
    private static final String BEGIN = "beginTime";
    private static final String END = "endTime";
    private static final String DESCRIPTION = "description";

    private final ConcurrentHashMap<String, AppointmentBook> apptBooks = new ConcurrentHashMap<>();

    /**
     * Handles an HTTP GET request from a client. If owner is the only supplied query parameter, then return that
     * owner's AppointmentBook pretty printed. Otherwise, do a search based on a time range and return those
     * appointments pretty printed.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        String owner = getParameter(OWNER_NAME, request);
        if (null == owner) {
            missingRequiredParameter(response, OWNER_NAME);
            return;
        }

        boolean isSearchPath = false;
        String beginTime = getParameter(BEGIN, request);
        String endTime = getParameter(END, request);
        // If exactly one parameter is null, this is an error. We need both parameters
        if (null == beginTime && null != endTime) {
            missingRequiredParameter(response, BEGIN);
            return;
        } else if (null != beginTime && null == endTime) {
            missingRequiredParameter(response, END);
            return;
        } else if (beginTime != null && endTime != null) {
            isSearchPath = true;
        }

        PrettyPrinter pp = new PrettyPrinter(response.getOutputStream());
        AppointmentBook book = apptBooks.get(owner);

        if (null == book) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, Messages.ownerNotFound(owner));
            return;
        }

        if (!isSearchPath) {
            pp.dump(book);
        } else {
            Collection<Appointment> apptCollection = book.getAppointments();
            AppointmentBook tempApptBook = new AppointmentBook(owner);
            Date beginDate = Appointment.parseStringIntoDate(beginTime);
            Date endDate = Appointment.parseStringIntoDate(endTime);
            for (Appointment appt : apptCollection) {
                if (appt.getBeginTime().compareTo(beginDate) >= 0 && appt.getEndTime().compareTo(endDate) <= 0) {
                    tempApptBook.addAppointment(appt);
                }
            }
            pp.dump(tempApptBook);
        }

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP POST request by parsing out the appointment book owner, description, begin, and end times.
     * It will then lookup an existing appointment book by owner name or if such an appointment book does not exist,
     * create one. It will then create an appointment and add it to the book by the given parameters.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(OWNER_NAME, getParameter(OWNER_NAME, request));
        params.put(DESCRIPTION, getParameter(DESCRIPTION, request));
        params.put(BEGIN, getParameter(BEGIN, request));
        params.put(END, getParameter(END, request));

        // Validate the input
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) it.next();
            if (null == entry.getValue()) {
                missingRequiredParameter(response, entry.getKey());
                return;
            }
        }


        Date start = Appointment.parseStringIntoDate(params.get(BEGIN));
        Date end = Appointment.parseStringIntoDate(params.get(END));
        if (null == start) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Invalid date format for startTime.");
            return;
        }
        if (null == end) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Invalid date format for endTime.");
            return;
        }

        AppointmentBook book = apptBooks.get(params.get(OWNER_NAME));
        if (null == book) {
            book = new AppointmentBook(params.get(OWNER_NAME));
            apptBooks.put(params.get(OWNER_NAME), book);
        }

        Appointment appointment = new Appointment(params.get(DESCRIPTION), params.get(BEGIN), params.get(END));
        book.addAppointment(appointment);

        response.getWriter().write(appointment.toString());
        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }


    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
