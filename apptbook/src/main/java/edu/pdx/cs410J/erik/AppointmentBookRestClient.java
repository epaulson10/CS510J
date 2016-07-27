package edu.pdx.cs410J.erik;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Send a request to the server to create an appointment
     *
     * @param owner The owner of the AppointmentBook in which to insert the appointment
     * @param description The description of the appointment
     * @param start The beginTime of the appointment
     * @param end The endTime of the appointment
     * @return The response from the server.
     * @throws IOException
     */
    public Response createAppointment(String owner, String description, String start, String end) throws IOException{
        return post(this.url, "owner", owner, "description", description, "beginTime", start, "endTime", end);
    }

    /**
     * Send a request to the server to search for appointments in a time range.
     *
     * @param owner The owner of the appointment book to search for.
     * @param start The start of the time range to search for in format MM/DD/YYYY HH:MM (am | pm)
     * @param end The end of the time range to search for in format MM/DD/YYYY HH:MM (am | pm)
     * @return The response from the server
     * @throws IOException
     */
    public Response searchAppointments(String owner, String start, String end) throws IOException{
        return get(this.url, "owner", owner, "beginTime", start, "endTime", end);
    }

}
