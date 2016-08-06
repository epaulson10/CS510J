package edu.pdx.cs410J.erik.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the ping service
 */
public interface PingServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void ping(AsyncCallback<AppointmentBook> async);

  void getAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  void searchAppointments(String owner, String beginTime, String endTime, AsyncCallback<AppointmentBook> async);

  void addAppointment(String owner, String description, String beginTime, String endTime, AsyncCallback<Boolean> async);

  void createAppointmentBook(String owner);
}
