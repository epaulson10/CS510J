package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {
  public static final String README = "By: Erik Paulson\n" +
          "CS410J Project 1: Designing an Appointment Book Application \n" +
          "A simple command line program that creates an AppointmentBook and Appointment with the given command line arguments.";

  public static final String DATE_WRONG_ERROR = "Date is not in 24-hour format: mm/dd/yyyy hh:mm";


  public static void main(String[] args) {
    Class c = AbstractAppointmentBook.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    boolean printDescription = false;

    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    // Convert the array to an ArrayList to work with the data easier
    ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

    // If -readme is passed, only print the README and do no more
    if (argList.contains("-readme")) {
      System.out.println(README);
      System.exit(0);
    }

    // If the print option is passed, save that info for later and remove that param
    if (argList.contains("-print")) {
      printDescription = true;
      argList.remove("-print");
    }

    // Validate the number of args
    if (argList.size() != 4) {
      System.err.println("Incorrect command line arguments");
      System.exit(1);
    }
    String owner = argList.get(0);
    String description = argList.get(1);
    String start = argList.get(2);
    String end = argList.get(3);

    if (description.equals("")) {
      System.err.println("Description cannot be empty");
      System.exit(1);
    }

    if (!isDateValid(start) || !isDateValid(end)) {
      System.err.println(DATE_WRONG_ERROR);
      System.exit(1);
    }

    // Create an AppointmentBook and Appointment based off of the command line arguments
    AppointmentBook book = new AppointmentBook(owner);
    Appointment appointment = new Appointment(description, start, end);
    book.addAppointment(appointment);

    if (printDescription) {
      System.out.println(appointment.toString());
    }

    System.exit(0);
  }

  /**
   * A helper function to validate the date passed in from the command line.
   * A valid date is of the form MM/dd/yyyy HH:mm
   *
   * @param date The date to validate
   * @return true if the date is valid, false otherwise
     */
  private static boolean isDateValid(String date) {
    // mm/dd/yyyy hh:mm
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    ParsePosition position = new ParsePosition(0);
    simpleDateFormat.parse(date, position);
    if (position.getIndex() == 0) {
      return false;
    }

    return true;
  }

}