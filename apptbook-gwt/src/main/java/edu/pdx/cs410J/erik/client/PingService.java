package edu.pdx.cs410J.erik.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote asyncService that interacts with remote {@link edu.pdx.cs410J.erik.client.AppointmentBook AppointmentBooks}
 */
@RemoteServiceRelativePath("ping")
public interface PingService extends RemoteService {

  public AppointmentBook getAppointmentBook(String owner);

  public void createAppointmentBook(String owner);

  public boolean addAppointment(String owner, String description, String beginTime, String endTime) throws IllegalArgumentException;

  public AppointmentBook searchAppointments(String owner, String beginTime, String endTime) throws IllegalArgumentException;

}
