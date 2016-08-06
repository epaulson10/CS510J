package edu.pdx.cs410J.erik.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("ping")
public interface PingService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  public AppointmentBook ping();

  public AppointmentBook getAppointmentBook(String owner);

  public void createAppointmentBook(String owner);

  public boolean addAppointment(String owner, String description, String beginTime, String endTime);

  public AppointmentBook searchAppointments(String owner, String beginTime, String endTime);

}
