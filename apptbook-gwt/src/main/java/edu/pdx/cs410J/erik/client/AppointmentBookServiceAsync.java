package edu.pdx.cs410J.erik.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the AppointmentBook service.
 */
public interface AppointmentBookServiceAsync {


  void getAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  void searchAppointments(String owner, String beginTime, String endTime, AsyncCallback<AppointmentBook> async);

  void addAppointment(String owner, String description, String beginTime, String endTime, AsyncCallback<Boolean> async);

  void createAppointmentBook(String owner, AsyncCallback<Void> async);
}
