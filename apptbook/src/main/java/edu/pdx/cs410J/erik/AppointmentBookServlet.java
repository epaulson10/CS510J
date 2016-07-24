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
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private static final String OWNER = "owner";
    private static final String BEGIN = "beginTime";
    private static final String END = "endTime";
    private static final String DESCRIPTION = "description";

    private final Map<String, String> data = new HashMap<>();
    private final ConcurrentHashMap<String, AppointmentBook> apptBooks = new ConcurrentHashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        String owner = getParameter(OWNER, request);
        if (null == owner) {
            missingRequiredParameter(response,OWNER);
        }

        boolean isSearchPath = false;
        String beginTime = getParameter(BEGIN, request);
        String endTime = getParameter(END, request);
        // If exactly one parameter is null, this is an error. We need both parameters
        if (null == beginTime && null != endTime) {
            missingRequiredParameter(response, BEGIN);
        } else if (null != beginTime && null == endTime) {
            missingRequiredParameter(response, END);
        } else if (beginTime != null && endTime != null) {
            isSearchPath = true;
        }

        PrettyPrinter pp = new PrettyPrinter(response.getOutputStream());
        AppointmentBook book = apptBooks.get(OWNER);

        if (null == book) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, Messages.ownerNotFound(OWNER));
            return;
        }

        if (!isSearchPath) {
            pp.dump(book);
        } else {
            Collection<Appointment> apptCollection = book.getAppointments();
            AppointmentBook tempApptBook = new AppointmentBook(OWNER);
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
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(OWNER, getParameter(OWNER, request));
        params.put(DESCRIPTION, getParameter(DESCRIPTION, request));
        params.put(BEGIN, getParameter(BEGIN, request));
        params.put(END, getParameter(END, request));

        // Validate the input
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) it.next();
            if (null == entry.getValue()) {
                missingRequiredParameter(response, entry.getKey());
            }
        }

        AppointmentBook book = apptBooks.get(params.get(OWNER));
        if (null == book) {
            book = new AppointmentBook(params.get(OWNER));
            apptBooks.put(OWNER, book);
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
        Appointment appointment = new Appointment(params.get(DESCRIPTION), params.get(BEGIN), params.get(END));
        book.addAppointment(appointment);

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all key/value pairs.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.data.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allMappingsDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

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
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and {@link Messages#formatKeyValuePair(String, String)}
     */
    private void writeValue( String key, HttpServletResponse response ) throws IOException
    {
        String value = this.data.get(key);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( value != null ? 1 : 0 ));
        pw.println(Messages.formatKeyValuePair(key, value));

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Writes all of the key/value pairs to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatKeyValuePair(String, String)}
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(data.size()));

        for (Map.Entry<String, String> entry : this.data.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
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

    @VisibleForTesting
    void setValueForKey(String key, String value) {
        this.data.put(key, value);
    }

    @VisibleForTesting
    String getValueForKey(String key) {
        return this.data.get(key);
    }
}
